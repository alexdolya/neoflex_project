package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dolya.deal.mapper.StatusHistoryMapper;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.dto.EmailMessage;
import ru.dolya.deal.model.dto.Theme;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.SesService;

@Service
@RequiredArgsConstructor
@Log4j2
public class SesServiceImpl implements SesService {
    private final ApplicationRepository applicationRepository;
    private final KafkaProducerService kafkaProducerService;
    private final StatusHistoryMapper statusHistoryMapper;
    @Value(value = "${kafka.credit-issued}")
    private String creditIssuedTopic;

    @Value(value = "${kafka.application-denied}")
    private String applicationDeniedTopic;

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
            application.setStatusHistory(statusHistoryMapper
                    .updateStatusHistory(application.getStatusHistory(), ApplicationStatus.CREDIT_ISSUED));

            kafkaProducerService.send(creditIssuedTopic, new EmailMessage()
                    .setApplicationId(applicationId)
                    .setAddress(application.getClient().getEmail())
                    .setTheme(Theme.CREDIT_ISSUED));

            applicationRepository.save(application);
        } else {
            kafkaProducerService.send(applicationDeniedTopic, new EmailMessage()
                    .setApplicationId(applicationId)
                    .setAddress(application.getClient().getEmail())
                    .setTheme(Theme.APPLICATION_DENIED));
            throw new RuntimeException(new Throwable("Wrong SES code"));
        }
    }
}
