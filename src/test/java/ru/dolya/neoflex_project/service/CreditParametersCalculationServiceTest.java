package ru.dolya.neoflex_project.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dolya.neoflex_project.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreditParametersCalculationServiceTest {
    @InjectMocks
    CreditParametersCalculationService creditParametersCalculationService;



    @Test
    void calculateAnnuityPayment() {
        assertEquals(BigDecimal.valueOf(2997.09), creditParametersCalculationService
                .calculateAnnuityPayment(BigDecimal.valueOf(100000), BigDecimal.valueOf(5), 36));
    }

    @Test
    void calculateTotalAmount() {
        assertEquals(BigDecimal.valueOf(107895.24), creditParametersCalculationService
                .calculateTotalAmount(BigDecimal.valueOf(2997.09), 36));
    }


    @Test
    void calculatePaymentSchedule() {
        BigDecimal totalPayment = BigDecimal.valueOf(26050.28);
        List<PaymentScheduleElement> paymentScheduleElementList = creditParametersCalculationService.calculatePaymentSchedule(
                totalPayment,
                BigDecimal.valueOf(100000),
                BigDecimal.valueOf(20),
                4);

        assertEquals(paymentScheduleElementList.get(0).getTotalPayment(), totalPayment);
        assertEquals(paymentScheduleElementList.get(0).getInterestPayment(), BigDecimal.valueOf(1698.63));
        assertEquals(paymentScheduleElementList.get(0).getDebtPayment(), BigDecimal.valueOf(24351.65));
        assertEquals(paymentScheduleElementList.get(0).getRemainingDebt(), BigDecimal.valueOf(75648.35));

        assertEquals(paymentScheduleElementList.get(1).getTotalPayment(), totalPayment);
        assertEquals(paymentScheduleElementList.get(1).getInterestPayment(), BigDecimal.valueOf(1284.99));
        assertEquals(paymentScheduleElementList.get(1).getDebtPayment(), BigDecimal.valueOf(24765.29));
        assertEquals(paymentScheduleElementList.get(1).getRemainingDebt(), BigDecimal.valueOf(50883.06));

        assertEquals(paymentScheduleElementList.get(2).getTotalPayment(), totalPayment);
        assertEquals(paymentScheduleElementList.get(2).getInterestPayment(), BigDecimal.valueOf(836.43));
        assertEquals(paymentScheduleElementList.get(2).getDebtPayment(), BigDecimal.valueOf(25213.85));
        assertEquals(paymentScheduleElementList.get(2).getRemainingDebt(), BigDecimal.valueOf(25669.21));

        assertEquals(paymentScheduleElementList.get(3).getTotalPayment(), totalPayment);
        assertEquals(paymentScheduleElementList.get(3).getInterestPayment(), BigDecimal.valueOf(436.02));
        assertEquals(paymentScheduleElementList.get(3).getDebtPayment(), BigDecimal.valueOf(25614.26));
        assertEquals(paymentScheduleElementList.get(3).getRemainingDebt(), BigDecimal.valueOf(54.95));

        assertEquals(paymentScheduleElementList.get(4).getTotalPayment(), BigDecimal.valueOf(54.95));
        assertEquals(paymentScheduleElementList.get(4).getInterestPayment(), BigDecimal.valueOf(0));
        assertEquals(paymentScheduleElementList.get(4).getDebtPayment(), BigDecimal.valueOf(54.95));
        assertEquals(paymentScheduleElementList.get(4).getRemainingDebt(), BigDecimal.valueOf(0));

    }

    @Test
    void calculateTotalAmountFromPaymentSchedule() {
        List<PaymentScheduleElement> paymentScheduleElementList = new ArrayList<>();
        paymentScheduleElementList.add(PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(125.25))
                .build());
        paymentScheduleElementList.add(PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(350.57))
                .build());
        paymentScheduleElementList.add(PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(481.12))
                .build());
        paymentScheduleElementList.add(PaymentScheduleElement.builder()
                .totalPayment(BigDecimal.valueOf(12.00))
                .build());
        assertEquals(BigDecimal.valueOf(968.94), creditParametersCalculationService.calculateTotalAmountFromPaymentSchedule(paymentScheduleElementList));
    }
}