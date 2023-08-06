package ru.dolya.conveyor.service;

import ru.dolya.conveyor.dto.CreditDTO;
import ru.dolya.conveyor.dto.ScoringDataDTO;


public interface CreditService {

    CreditDTO getCredit(ScoringDataDTO scoringDataDTO);
}
