package ru.dolya.dossier.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.dolya.dossier.dto.EmailMessage;

@Service
@Log4j2
public class KafkaConsumer {

    @KafkaListener(topics = "finish-registration", groupId = "neoflex-group")
    public void consumeEmailMessage(EmailMessage emailMessage) {
        log.info("Received email message: " + emailMessage);
    }
}