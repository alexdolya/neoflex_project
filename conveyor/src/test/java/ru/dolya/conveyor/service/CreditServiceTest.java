package ru.dolya.conveyor.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.conveyor.ConveyorApplication;
import ru.dolya.conveyor.dto.CreditDTO;
import ru.dolya.conveyor.dto.EmploymentDTO;
import ru.dolya.conveyor.dto.PaymentScheduleElement;
import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.dto.enums.EmploymentStatus;
import ru.dolya.conveyor.dto.enums.Gender;
import ru.dolya.conveyor.dto.enums.MartialStatus;
import ru.dolya.conveyor.dto.enums.Position;
import ru.dolya.conveyor.manager.CreditRulesManager;
import ru.dolya.conveyor.manager.ScoringManager;
import ru.dolya.conveyor.manager.impl.CreditParametersCalculationManagerImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConveyorApplication.class)
class CreditServiceTest {
    @Mock
    private ScoringManager scoringManager;
    @Mock
    private CreditRulesManager creditRulesManager;
    @Mock
    private CreditParametersCalculationManagerImpl creditParametersCalculationService;
    @Autowired
    CreditService creditService;

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
                .martialStatus(MartialStatus.MARRIED)
                .dependentAmount(1)
                .employment(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.SELF_EMPLOYED)
                        .salary(BigDecimal.valueOf(60000))
                        .position(Position.MID_MANAGER)
                        .workExperienceTotal(24)
                        .workExperienceCurrent(12)
                        .build())
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build();

        LocalDate date = LocalDate.of(2023,8,1);

        paymentSchedule = new ArrayList<>();
        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(1)
                .date(date)
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(1104.11))
                .debtPayment(BigDecimal.valueOf(32954.04))
                .remainingDebt(BigDecimal.valueOf(67045.96))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(2)
                .date(date.plusMonths(1))
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(716.38))
                .debtPayment(BigDecimal.valueOf(33341.77))
                .remainingDebt(BigDecimal.valueOf(33704.19))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(3)
                .date(date.plusMonths(2))
                .totalPayment(BigDecimal.valueOf(34058.15))
                .interestPayment(BigDecimal.valueOf(372.13))
                .debtPayment(BigDecimal.valueOf(33686.02))
                .remainingDebt(BigDecimal.valueOf(18.17))
                .build());

        paymentSchedule.add(PaymentScheduleElement.builder()
                .number(4)
                .date(date.plusMonths(3))
                .totalPayment(BigDecimal.valueOf(18.17))
                .interestPayment(BigDecimal.valueOf(0))
                .debtPayment(BigDecimal.valueOf(18.17))
                .remainingDebt(BigDecimal.valueOf(0))
                .build());

        creditDTOExpected = CreditDTO.builder()
                .amount(BigDecimal.valueOf(100000))
                .term(3)
                .monthlyPayment(BigDecimal.valueOf(34058.15))
                .rate(BigDecimal.valueOf(13))
                .psk(BigDecimal.valueOf(102192.62))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .paymentSchedule(paymentSchedule)
                .build();

    }



    @Test
    void getCredit() {
        when(scoringManager.calculateRate(any())).thenReturn(BigDecimal.valueOf(13));
        when(creditParametersCalculationService.calculateAnnuityPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(34058.15));
        when(creditParametersCalculationService.calculatePaymentSchedule(any(), any(), any(), anyInt())).thenReturn(paymentSchedule);
        when(creditParametersCalculationService.calculateTotalAmountFromPaymentSchedule(any())).thenReturn(BigDecimal.valueOf(102204.75));


        MockedStatic<LocalDate> guid1 = mockStatic(LocalDate.class, Mockito.CALLS_REAL_METHODS);
        LocalDate fixedDate = LocalDate.of(2023, 8, 1);
        when(LocalDate.now()).thenReturn(fixedDate);
        CreditDTO creditDTOActual = creditService.getCredit(scoringDataDTO);
        assertEquals(creditDTOExpected, creditDTOActual);
    }
}