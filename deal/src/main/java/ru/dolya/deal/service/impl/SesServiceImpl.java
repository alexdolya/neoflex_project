package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.StatusHistory;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;
import ru.dolya.deal.model.dto.EmailMessage;
import ru.dolya.deal.model.dto.Theme;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.SesService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class SesServiceImpl implements SesService {

    private final ApplicationRepository applicationRepository;
    private final KafkaProducerService kafkaProducerService;

    @Override
    public void setSes(Long applicationId, Integer ses) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setSesCode(ses);
        applicationRepository.save(application);
        log.info("Set ses code: {} to application №{}", ses, applicationId);
    }

    @Override
    public void verifySes(Long applicationId, Integer sesCode) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        if (application.getSesCode().equals(sesCode)) {
            application.setStatus(ApplicationStatus.CREDIT_ISSUED);
            List<StatusHistory> statusHistoryList = application.getStatusHistory();
            statusHistoryList.add(StatusHistory.builder()
                    .changeType(ChangeType.AUTOMATIC)
                    .status(ApplicationStatus.CREDIT_ISSUED)
                    .time(LocalDateTime.now())
                    .build());
            application.setStatusHistory(statusHistoryList);

            EmailMessage emailMessage = new EmailMessage()
                    .setApplicationId(applicationId)
                    .setAddress(application.getClient().getEmail())
                    .setTheme(Theme.CREDIT_ISSUED);
            kafkaProducerService.send("credit-issued", emailMessage);
            log.info("Send email message : {} to credit-issued topic", emailMessage);
            applicationRepository.save(application);
        } else {
            throw new RuntimeException(new Throwable("Wrong SES code"));
        }
    }
}
