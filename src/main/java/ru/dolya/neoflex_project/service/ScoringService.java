package ru.dolya.neoflex_project.service;

import ru.dolya.neoflex_project.dto.CreditDTO;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface ScoringService {

    BigDecimal calculateRate(ScoringDataDTO scoringDataDTO);
}
