package ru.dolya.conveyor.manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.conveyor.ConveyorApplication;
import ru.dolya.conveyor.dto.EmploymentDTO;
import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.dto.enums.EmploymentStatus;
import ru.dolya.conveyor.dto.enums.Gender;
import ru.dolya.conveyor.dto.enums.MaritalStatus;
import ru.dolya.conveyor.dto.enums.Position;
import ru.dolya.conveyor.exception.ScoringException;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConveyorApplication.class)
class ScoringManagerTest {

    @Mock
    CreditRulesManager creditRulesManager;
    @Autowired
    private ScoringManager scoringManager;
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
        when(creditRulesManager.getBaseRate()).thenReturn(BigDecimal.valueOf(20));

        assertEquals(BigDecimal.valueOf(13), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        assertEquals(BigDecimal.valueOf(15), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setPosition(Position.TOP_MANAGER);
        assertEquals(BigDecimal.valueOf(13), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.setMaritalStatus(MaritalStatus.DIVORCED);
        assertEquals(BigDecimal.valueOf(17), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.setDependentAmount(5);
        assertEquals(BigDecimal.valueOf(18), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.setGender(Gender.NON_BINARY);
        assertEquals(BigDecimal.valueOf(24), scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.UNEMPLOYED);
        assertThrows(ScoringException.class, () -> scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setEmploymentStatus(EmploymentStatus.BUSINESS_OWNER);
        scoringDataDTO.setAmount(BigDecimal.valueOf(100_000_000));
        assertThrows(ScoringException.class, () -> scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.setAmount(BigDecimal.valueOf(100_000));
        scoringDataDTO.setBirthdate(LocalDate.of(2020, 5, 30));
        assertThrows(ScoringException.class, () -> scoringManager.calculateRate(scoringDataDTO));


        scoringDataDTO.setBirthdate(LocalDate.of(1990, 5, 30));
        scoringDataDTO.getEmployment().setWorkExperienceTotal(1);
        assertThrows(ScoringException.class, () -> scoringManager.calculateRate(scoringDataDTO));

        scoringDataDTO.getEmployment().setWorkExperienceTotal(20);
        scoringDataDTO.getEmployment().setWorkExperienceCurrent(1);
        assertThrows(ScoringException.class, () -> scoringManager.calculateRate(scoringDataDTO));
    }


}