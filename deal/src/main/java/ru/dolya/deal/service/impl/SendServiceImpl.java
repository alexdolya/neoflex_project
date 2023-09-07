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
import ru.dolya.deal.service.SendService;

@Service
@RequiredArgsConstructor
@Log4j2
public class SendServiceImpl implements SendService {

    private final ApplicationRepository applicationRepository;

    private final KafkaProducerService kafkaProducerService;
    private final StatusHistoryMapper statusHistoryMapper;

    @Value(value = "${kafka.send-documents}")
    private String sendDocumentsTopic;

    @Override
    public void send(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(ApplicationStatus.PREPARE_DOCUMENTS);

        application.setStatusHistory(statusHistoryMapper
                .updateStatusHistory(application.getStatusHistory(), ApplicationStatus.PREPARE_DOCUMENTS));

        applicationRepository.save(application);
        log.info("Application status set to PREPARE_DOCUMENTS");

        kafkaProducerService.send(sendDocumentsTopic, new EmailMessage()
                .setApplicationId(applicationId)
                .setTheme(Theme.SEND_DOCUMENTS)
                .setAddress(application.getClient().getEmail()));
    }
}
