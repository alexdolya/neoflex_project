package ru.dolya.conveyor.service;

import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;
import ru.dolya.conveyor.dto.LoanOfferDTO;

import java.util.List;

public interface OfferService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO);
    LoanOfferDTO getOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO requestDTO);

}
