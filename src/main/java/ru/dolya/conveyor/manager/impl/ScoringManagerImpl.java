package ru.dolya.conveyor.manager.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.exception.ScoringException;
import ru.dolya.conveyor.manager.CreditRulesManager;
import ru.dolya.conveyor.manager.ScoringManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
@Log4j2
@Component
public class ScoringManagerImpl implements ScoringManager {

    private final CreditRulesManager creditRulesManager;

    public ScoringManagerImpl(CreditRulesManager creditRulesManager) {
        this.creditRulesManager = creditRulesManager;
    }


    @Override
    public BigDecimal calculateRate(ScoringDataDTO scoringDataDTO) {

        BigDecimal rate = creditRulesManager.getBaseRate();

        switch (scoringDataDTO.getEmployment().getEmploymentStatus()) {
            case UNEMPLOYED:
                throw new ScoringException(new Throwable("Reject - employment status is unemployed"));
            case SELF_EMPLOYED:
                rate = rate.add(BigDecimal.ONE);
                break;
            case BUSINESS_OWNER:
                rate = rate.add(BigDecimal.valueOf(3));
                break;
        }

        if (scoringDataDTO.getAmount().compareTo(scoringDataDTO.getEmployment().getSalary().multiply(BigDecimal.valueOf(20))) > 0) {
            throw new ScoringException(new Throwable("Reject - the requested amount is less than 20 salaries"));
        }

        int age = Period.between(scoringDataDTO.getBirthdate(), LocalDate.now()).getYears();

        if (age > 60 || age < 20) {
            throw new ScoringException(new Throwable("Reject - age less than 20 or more than 60"));
        }

        if (scoringDataDTO.getEmployment().getWorkExperienceTotal() < 12) {
            throw new ScoringException(new Throwable("Reject - total work experience less than 12 month"));
        }

        if (scoringDataDTO.getEmployment().getWorkExperienceCurrent() < 3) {
            throw new ScoringException(new Throwable("Reject - current work experience less than 3 month"));
        }

        switch (scoringDataDTO.getEmployment().getPosition()) {
            case MIDDLE_MANAGER:
                rate = rate.subtract(BigDecimal.valueOf(2));
                break;
            case TOP_MANAGER:
                rate = rate.subtract(BigDecimal.valueOf(4));
                break;
        }

        switch (scoringDataDTO.getMaritalStatus()) {
            case MARRIED:
                rate = rate.subtract(BigDecimal.valueOf(3));
                break;
            case DIVORCED:
                rate = rate.add(BigDecimal.valueOf(1));
                break;
        }

        if (scoringDataDTO.getDependentAmount() > 1) {
            rate = rate.add(BigDecimal.valueOf(1));
        }


        switch (scoringDataDTO.getGender()) {
            case MALE:
                if (age >= 30 && age <= 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                }
                break;
            case FEMALE:
                if (age >= 35 && age <= 60) {
                    rate = rate.subtract(BigDecimal.valueOf(3));
                }
                break;
            case NON_BINARY:
                rate = rate.add(BigDecimal.valueOf(3));
                break;
        }

        log.debug("Scoring completed. Rate = {}", rate);

        return rate;
    }
}
