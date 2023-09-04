package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.dto.EmailMessage;
import ru.dolya.deal.model.dto.Theme;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.SignService;

@Service
@RequiredArgsConstructor
@Log4j2
public class SignServiceImpl implements SignService {

    private final ApplicationRepository applicationRepository;
    private final KafkaProducerService kafkaProducerService;
    @Value(value = "${kafka.topic4}")
    private String sendSesTopic;



    @Override
    public void sign(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();

        EmailMessage emailMessage = new EmailMessage()
                .setApplicationId(applicationId)
                .setAddress(application.getClient().getEmail())
                .setTheme(Theme.SEND_SES);

        kafkaProducerService.send(sendSesTopic, emailMessage);
    }
}
