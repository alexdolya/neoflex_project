package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.mapper.CreditMapper;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.Credit;
import ru.dolya.deal.model.domain.StatusHistory;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;
import ru.dolya.deal.model.dto.EmailMessage;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.model.dto.Theme;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.OfferService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final ApplicationRepository applicationRepository;
    private final CreditMapper creditMapper;
    private final KafkaProducerService kafkaProducerService;

    @Value(value = "${kafka.finish-registration}")
    private String finishRegistrationTopic;

    @Transactional
    @Override
    public void getOffer(LoanOfferDTO loanOfferDTO) {

        Long appId = loanOfferDTO.getApplicationId();

        Application application = applicationRepository.findById(appId).orElseThrow();

        log.info("Successful load application from database");

        List<StatusHistory> statusHistory = application.getStatusHistory();

        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }


        statusHistory.add(StatusHistory.builder()
                .changeType(ChangeType.AUTOMATIC)
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .build());

        log.info("Set application status to APPROVED");

        Credit credit = creditMapper.getCreditFromRequestDTO(loanOfferDTO);

        application.setStatus(ApplicationStatus.APPROVED);
        application.setStatusHistory(statusHistory);
        application.setCredit(credit);
        application.setAppliedOffer(loanOfferDTO);

        applicationRepository.save(application);
        log.info("Save application to database");

        kafkaProducerService.send(finishRegistrationTopic, new EmailMessage()
                .setApplicationId(application.getApplicationId())
                .setAddress(application.getClient().getEmail())
                .setTheme(Theme.FINISH_REGISTRATION));
    }
}

