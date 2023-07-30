package ru.dolya.neoflex_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dolya.neoflex_project.dto.EmploymentDTO;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;
import ru.dolya.neoflex_project.dto.enums.EmploymentStatus;
import ru.dolya.neoflex_project.dto.enums.Gender;
import ru.dolya.neoflex_project.dto.enums.MaritalStatus;
import ru.dolya.neoflex_project.dto.enums.Position;
import ru.dolya.neoflex_project.exception.PreScoringException;
import ru.dolya.neoflex_project.exception.ScoringException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScoringServiceImplTest {

    @Mock
    CreditRules creditRules;
    @InjectMocks
    private ScoringServiceImpl scoringService;
    private ScoringDataDTO scoringDataDTO;

    @BeforeEach
    void init() {
        scoringDataDTO = ScoringDataDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(3)
                .firstName("Ivan")
                .lastName("Ivanov")
                .gender(Gender.MALE)
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("123456")
                .maritalStatus(MaritalStatus.MARRIED)
                .dependentAmount(1)
                .employment(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(60000))
                        .position(Position.MIDDLE_MANAGER)
                        .workExperienceTotal(24)
                        .workExperienceCurrent(12)
                        .build())
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();
    }

    @Test
    void calculateRate() {
        when(creditRules.getBaseRate()).thenReturn(BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(13), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        assertEquals(BigDecimal.valueOf(15), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setPosition(Position.TOP_MANAGER);
        assertEquals(BigDecimal.valueOf(13), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.setMaritalStatus(MaritalStatus.DIVORCED);
        assertEquals(BigDecimal.valueOf(17), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.setDependentAmount(5);
        assertEquals(BigDecimal.valueOf(18), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.setGender(Gender.NON_BINARY);
        assertEquals(BigDecimal.valueOf(24), scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        scoringDataDTO.setAmount(BigDecimal.valueOf(100_000_000));
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.setAmount(BigDecimal.valueOf(100_000));
        scoringDataDTO.setBirthdate(LocalDate.of(2020, 5, 30));
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDTO));


        scoringDataDTO.setBirthdate(LocalDate.of(1990, 5, 30));
        scoringDataDTO.getEmployment().setWorkExperienceTotal(1);
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setWorkExperienceTotal(20);
        scoringDataDTO.getEmployment().setWorkExperienceCurrent(1);
        assertThrows(ScoringException.class, () -> scoringService.calculateRate(scoringDataDTO));
    }


}