package ru.dolya.neoflex_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.exception.PreScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PreScoringServiceImplTest {

    @InjectMocks
    private PreScoringServiceImpl preScoringService;


    @Test
    public void testPreScoring_Valid() {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("ivanov@mail.ru")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
        boolean result = preScoringService.preScoring(requestDTO);
        assertTrue(result);
    }

    @Test
    public void testPreScoring_Invalid() {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(100000))
                .term(0)
                .birthdate(LocalDate.of(2015, 1, 1))
                .email("ivanov@mail.ru")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
        assertThrows(PreScoringException.class, () -> preScoringService.preScoring(requestDTO));
    }

}