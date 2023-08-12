package ru.dolya.deal.service;

import ru.dolya.deal.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.dto.LoanOfferDTO;

import java.util.List;

public interface ApplicationService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO);

}
