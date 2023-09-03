package ru.dolya.deal.service;

import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;

public interface SendService {
    void send(Long applicationId);
}
