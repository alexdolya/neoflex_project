package ru.dolya.conveyor.manager;

import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;

public interface PreScoringManager {
    boolean preScoring(LoanApplicationRequestDTO requestDTO);
}
