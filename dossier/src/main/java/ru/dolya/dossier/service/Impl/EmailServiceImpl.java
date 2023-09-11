package ru.dolya.dossier.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.dolya.dossier.client.DealApi;
import ru.dolya.dossier.dto.EmailMessage;
import ru.dolya.dossier.dto.Theme;
import ru.dolya.dossier.exception.FeignClientCustomException;
import ru.dolya.dossier.service.EmailService;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {


    private final LetterContentService letterContentService;
    private final JavaMailSender emailSender;
    private final DealApi dealApi;

    @Override
    public void sendMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("${spring.mail.username}");
        message.setTo(emailMessage.getAddress());
        message.setSubject(emailMessage.getTheme().getMessage());
        if (emailMessage.getTheme().equals(Theme.SEND_SES)) {
            int randomNumber = getRandomSes();
            try {
                dealApi.setSes(emailMessage.getApplicationId(), randomNumber);
            } catch (Exception ex) {
                throw new FeignClientCustomException(new Throwable(ex.getMessage()));
            }
            message.setText("Your SES code: " + randomNumber);
        } else {
            message.setText("For your application №:" + emailMessage.getApplicationId() + ". " + letterContentService.getMailBodyByEmailMessage(emailMessage));
        }
        try {
            emailSender.send(message);
        } catch (MailException exception) {
            throw new RuntimeException(exception.getMessage());
        }

        log.info("Message sent to: {}, with theme: {}", emailMessage.getAddress(), emailMessage.getTheme());
    }

    private int getRandomSes() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }


}
