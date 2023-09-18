package ru.dolya.dossier.service.Impl;

import org.springframework.stereotype.Service;
import ru.dolya.dossier.dto.EmailMessage;

@Service
public class LetterContentService {

    public String getMailBodyByEmailMessage(EmailMessage emailMessage) {
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION:
                return "Finish registration. Go to next step to calculate offer.";
            case CREATE_DOCUMENT:
                return "Please prepare your documents for credit";
            case SEND_DOCUMENTS:
                return "Here is your loan documents";
            case CREDIT_ISSUED:
                return "You got your credit! Congrats!";
            case APPLICATION_DENIED:
                return "Sorry. Application denied";
            default:
                return "None text";
        }
    }
}

