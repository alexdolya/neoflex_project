package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import ru.dolya.deal.model.dto.EmailMessage;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducerService {
    private final KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void send(String topic, EmailMessage message) {
        try {
            kafkaTemplate.send(topic, message)
                    .completable()
                    .whenComplete(
                            (result, ex) -> {
                                if (ex == null) {
                                    log.info("Send email message: {} to topic: {}", message, topic);

                                } else {
                                    log.error("Message: {} was not sent to topic: {}", message, topic, ex);
                                }
                            });
        } catch (Exception ex) {
            log.error("Send error, message: {} was not sent to topic: {}", message, topic, ex);
        }
    }
}
