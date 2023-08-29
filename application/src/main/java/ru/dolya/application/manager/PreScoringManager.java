package ru.dolya.application.manager;

import ru.dolya.application.dto.LoanApplicationRequestDTO;


public interface PreScoringManager {
    boolean preScoring(LoanApplicationRequestDTO requestDTO);
}


