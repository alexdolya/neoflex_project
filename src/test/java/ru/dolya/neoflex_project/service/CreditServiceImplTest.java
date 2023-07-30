package ru.dolya.neoflex_project.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dolya.neoflex_project.dto.CreditDTO;
import ru.dolya.neoflex_project.dto.EmploymentDTO;
import ru.dolya.neoflex_project.dto.PaymentScheduleElement;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;
import ru.dolya.neoflex_project.dto.enums.EmploymentStatus;
import ru.dolya.neoflex_project.dto.enums.Gender;
import ru.dolya.neoflex_project.dto.enums.MaritalStatus;
import ru.dolya.neoflex_project.dto.enums.Position;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceImplTest {
    @Mock
    private ScoringService scoringService;
    @Mock
    private CreditRules creditRules;
    @Mock
    private CreditParametersCalculationService creditParametersCalculationService;
    @InjectMocks
    CreditServiceImpl creditService;

    private ScoringDataDTO scoringDataDTO;
    private List<PaymentScheduleElement> paymentSchedule;
    private CreditDTO creditDTOExpected;


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

        paymentSchedule = new ArrayList<>();
        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(1)
                .date(LocalDate.parse("2023-07-30"))
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(1104.11))
                .debtPayment(BigDecimal.valueOf(32954.04))
                .remainingDebt(BigDecimal.valueOf(67045.96))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(2)
                .date(LocalDate.parse("2023-08-30"))
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(740.26))
                .debtPayment(BigDecimal.valueOf(33317.89))
                .remainingDebt(BigDecimal.valueOf(33728.07))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(3)
                .date(LocalDate.parse("2023-09-30"))
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(360.38))
                .debtPayment(BigDecimal.valueOf(33697.77))
                .remainingDebt(BigDecimal.valueOf(30.30))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(4)
                .date(LocalDate.parse("2023-10-30"))
                .totalPayment(BigDecimal.valueOf(30.30))
                .interestPayment(BigDecimal.valueOf(0))
                .debtPayment(BigDecimal.valueOf(30.30))
                .remainingDebt(BigDecimal.valueOf(0))
                .build());

        creditDTOExpected = CreditDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(3)
                .monthlyPayment(BigDecimal.valueOf(34058.15))
                .rate(BigDecimal.valueOf(13.0))
                .psk(BigDecimal.valueOf(102204.75))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .paymentSchedule(paymentSchedule)
                .build();

    }



    @Test
    void getCredit() {
        when(scoringService.calculateRate(any())).thenReturn(BigDecimal.valueOf(13.0));
        when(creditParametersCalculationService.calculateAnnuityPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(34058.15));
        when(creditParametersCalculationService.calculatePaymentSchedule(any(), any(), any(), anyInt())).thenReturn(paymentSchedule);
        when(creditParametersCalculationService.calculateTotalAmountFromPaymentSchedule(any())).thenReturn(BigDecimal.valueOf(102204.75));

        CreditDTO creditDTOActual = creditService.getCredit(scoringDataDTO);

        assertEquals(creditDTOExpected, creditDTOActual);
    }
}