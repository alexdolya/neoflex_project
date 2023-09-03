package ru.dolya.dossier.service.Impl;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.dolya.dossier.client.DealApi;
import ru.dolya.dossier.dto.EmailMessage;
import ru.dolya.dossier.dto.Theme;
import ru.dolya.dossier.service.EmailService;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {


    private final LetterContentService letterContentService;
    private final JavaMailSender emailSender;
    private final DealApi dealApi;

    @Override
    public void sendMessage(EmailMessage emailMessage) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("${spring.mail.username}");
        message.setTo(emailMessage.getAddress());
        message.setSubject(letterContentService.getStringThemeByEmailMessage(emailMessage));
        if (emailMessage.getTheme() == Theme.SEND_SES) {
            int randomNumber = getRandomSes();
            dealApi.setSes(emailMessage.getApplicationId(), randomNumber);
            message.setText("Your SES code: " + randomNumber);
        } else {
            message.setText("For your application №:" + emailMessage.getApplicationId() + ". " + letterContentService.getMailBodyByEmailMessage(emailMessage));
        }
        emailSender.send(message);
    }

    private int getRandomSes() {
        Random random = new Random();
        return random.nextInt(900000) + 100000;
    }


}
