package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import ru.dolya.deal.model.dto.EmailMessage;
import ru.dolya.deal.model.dto.Theme;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Log4j2
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, EmailMessage> kafkaTemplate;

    public void send(EmailMessage message) {
        try {
            log.info("value:{}", message);
            kafkaTemplate.send("finish-registration", message)
                    .completable()
                    .whenComplete(
                            (result, ex) -> {
                                if (ex == null) {
                                    log.info(
                                            "message id:{} was sent, offset:{}",
                                            message,
                                            result.getRecordMetadata().offset());
                                } else {
                                    log.error("message id:{} was not sent", message, ex);
                                }
                            });
        } catch (Exception ex) {
            log.error("send error, value:{}", message, ex);
        }
    }
}
