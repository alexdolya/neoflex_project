package ru.dolya.neoflex_project.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.exception.PreScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.regex.Pattern;

@Log4j2
@Service
public class PreScoringServiceImpl implements PreScoringService {

    @Override
    public boolean preScoring(LoanApplicationRequestDTO requestDTO) {
        if (!validateLatinLetters(requestDTO.getFirstName()) || !validateLatinLetters(requestDTO.getLastName())) {
            throw new PreScoringException(new Throwable("Wrong parameter: First name or Last name"));
        }
        if (requestDTO.getMiddleName() != null && !validateLatinLetters(requestDTO.getMiddleName())) {
            throw new PreScoringException(new Throwable("Wrong parameter: Middle name"));
        }
        if (requestDTO.getAmount() == null || requestDTO.getAmount().compareTo(BigDecimal.valueOf(10000)) < 0) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: Amount"));
        }
        if (requestDTO.getTerm() == null || requestDTO.getTerm() < 6) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: Term"));
        }
        if (requestDTO.getBirthdate() == null || ChronoUnit.YEARS.between(requestDTO.getBirthdate(), LocalDate.now()) < 18L) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: Birthdate"));
        }
        String emailPattern = "[\\w\\.]{2,50}@[\\w\\.]{2,20}";
        if (requestDTO.getEmail() == null || !Pattern.matches(emailPattern, requestDTO.getEmail())) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: email"));
        }
        String passportSeriesPattern = "\\d{4}";
        if (requestDTO.getPassportSeries() == null || !Pattern.matches(passportSeriesPattern, requestDTO.getPassportSeries())) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: Passport Series"));
        }
        String passportNumberPattern = "\\d{6}";
        if (requestDTO.getPassportNumber() == null || !Pattern.matches(passportNumberPattern, requestDTO.getPassportNumber())) {
            throw new PreScoringException(new Throwable("Wrong or null parameter: Passport Number"));
        }
        log.info("Prescoring for {} {} was successful", requestDTO.getFirstName(), requestDTO.getLastName());
        return true;
    }

    private boolean validateLatinLetters(String string) {
        if (string == null || string.length() < 2 || string.length() > 30) {
            return false;
        }
        for (char c : string.toCharArray()) {
            if (!Character.isLetter(c)) {
                return false;
            }
        }
        return true;
    }
}


