package ru.dolya.deal.service;

import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;

public interface CalculateByIdService {
    void calculateById(FinishRegistrationRequestDTO requestDTO, Long applicationId);

}
