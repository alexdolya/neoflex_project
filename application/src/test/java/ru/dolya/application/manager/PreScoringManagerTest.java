package ru.dolya.application.manager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.application.Application;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.exception.PreScoringException;
import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
public class PreScoringManagerTest {

    @Autowired
    private PreScoringManager preScoringManager;


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
        boolean result = preScoringManager.preScoring(requestDTO);
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
        assertThrows(PreScoringException.class, () -> preScoringManager.preScoring(requestDTO));
    }

}