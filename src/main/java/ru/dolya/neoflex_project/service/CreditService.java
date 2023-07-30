package ru.dolya.neoflex_project.service;

import org.springframework.stereotype.Service;
import ru.dolya.neoflex_project.dto.CreditDTO;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;


public interface CreditService {

    CreditDTO getCredit(ScoringDataDTO scoringDataDTO);
}
