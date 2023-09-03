package ru.dolya.dossier.service.Impl;

import org.springframework.stereotype.Service;
import ru.dolya.dossier.dto.EmailMessage;

@Service
public class LetterContentService {

    public String getStringThemeByEmailMessage(EmailMessage emailMessage) {
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION:
                return "Finish registration";
            case CREATE_DOCUMENT:
                return "Create documents";
            case SEND_DOCUMENTS:
                return "Your loan documents";
            case SEND_SES:
                return "SES code";
            case CREDIT_ISSUED:
                return "Credit issued";
            case APPLICATION_DENIED:
                return "Application denied";
            default:
                return "None theme";
        }
    }

    public String getMailBodyByEmailMessage(EmailMessage emailMessage){
        switch (emailMessage.getTheme()) {
            case FINISH_REGISTRATION:
                return "Finish registration. Proceed \"deal/calculate/{applicationId}\" to calculate offer.";
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

