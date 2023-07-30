package ru.dolya.neoflex_project.service;

import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;

public interface PreScoringService {
    boolean preScoring(LoanApplicationRequestDTO requestDTO);
}
