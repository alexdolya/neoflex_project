package ru.dolya.dossier.service;

import ru.dolya.dossier.dto.EmailMessage;

public interface EmailService {
    void sendMessage(EmailMessage emailMessage);
}
