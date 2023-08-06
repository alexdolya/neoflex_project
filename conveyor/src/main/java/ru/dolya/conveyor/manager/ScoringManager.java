package ru.dolya.conveyor.manager;

import ru.dolya.conveyor.dto.ScoringDataDTO;

import java.math.BigDecimal;

public interface ScoringManager {

    BigDecimal calculateRate(ScoringDataDTO scoringDataDTO);
}
