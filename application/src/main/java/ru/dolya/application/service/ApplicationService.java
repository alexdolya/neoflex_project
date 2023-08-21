package ru.dolya.application.service;

import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO);
}
