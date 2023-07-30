package ru.dolya.neoflex_project.service;

import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.dto.LoanOfferDTO;

import java.util.List;

public interface OfferService {
    List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO);
}
