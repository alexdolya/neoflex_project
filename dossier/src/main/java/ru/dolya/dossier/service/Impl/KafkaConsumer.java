package ru.dolya.dossier.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.dolya.dossier.client.DealApi;
import ru.dolya.dossier.dto.EmailMessage;
import ru.dolya.dossier.exception.FeignClientCustomException;
import ru.dolya.dossier.service.EmailService;

@Service
@Log4j2
@RequiredArgsConstructor
public class KafkaConsumer {

    private final DealApi dealApi;
    private final EmailService emailService;

    @KafkaListener(topics = "${kafka.topic1}", groupId = "neoflex-group")
    public void consumeEmailMessageFromFinishRegistration(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topic2}", groupId = "neoflex-group")
    public void consumeEmailMessageFromCreateDocuments(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topic3}", groupId = "neoflex-group")
    public void consumeEmailMessageFromSendDocuments(EmailMessage emailMessage) {
        try {
            dealApi.status(emailMessage.getApplicationId());
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
        emailService.sendMessage(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topic4}", groupId = "neoflex-group")
    public void consumeEmailMessageFromSendSes(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topic5}", groupId = "neoflex-group")
    public void consumeEmailMessageFromCreditIssued(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }

    @KafkaListener(topics = "${kafka.topic6}", groupId = "neoflex-group")
    public void consumeEmailMessageFromApplicationDenied(EmailMessage emailMessage) {
        emailService.sendMessage(emailMessage);
    }
}