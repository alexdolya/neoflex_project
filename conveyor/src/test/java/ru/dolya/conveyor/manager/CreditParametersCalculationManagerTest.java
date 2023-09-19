package ru.dolya.conveyor.manager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.conveyor.ConveyorApplication;
import ru.dolya.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConveyorApplication.class)
class CreditParametersCalculationManagerTest {
    @Autowired
    CreditParametersCalculationManager creditParametersCalculationManager;

    @Test
    void calculateAnnuityPayment() {
        assertEquals(BigDecimal.valueOf(2997.09), creditParametersCalculationManager
                .calculateAnnuityPayment(BigDecimal.valueOf(100000), BigDecimal.valueOf(5), 36));
    }

    @Test
    void calculateTotalAmount() {
        assertEquals(BigDecimal.valueOf(107895.24), creditParametersCalculationManager
                .calculateTotalAmount(BigDecimal.valueOf(2997.09), 36));
    }


//    @Test
//    void calculatePaymentSchedule() {
//        BigDecimal totalPayment = BigDecimal.valueOf(26050.28);
//        List<PaymentScheduleElement> paymentScheduleElementList = creditParametersCalculationManager.calculatePaymentSchedule(
//                totalPayment,
//                BigDecimal.valueOf(100000),
//                BigDecimal.valueOf(20),
//                4);
//
//        assertEquals(totalPayment, paymentScheduleElementList.get(0).getTotalPayment());
//        assertEquals(BigDecimal.valueOf(1698.63), paymentScheduleElementList.get(0).getInterestPayment());
//        assertEquals(BigDecimal.valueOf(24351.65), paymentScheduleElementList.get(0).getDebtPayment());
//        assertEquals(BigDecimal.valueOf(75648.35), paymentScheduleElementList.get(0).getRemainingDebt());
//
//
//
//        assertEquals(totalPayment,paymentScheduleElementList.get(1).getTotalPayment());
//        assertEquals(BigDecimal.valueOf(1243.53), paymentScheduleElementList.get(1).getInterestPayment());
//        assertEquals(BigDecimal.valueOf(24806.75), paymentScheduleElementList.get(1).getDebtPayment());
//        assertEquals(BigDecimal.valueOf(50841.60).setScale(2, RoundingMode.HALF_UP), paymentScheduleElementList.get(1).getRemainingDebt());
//
//        assertEquals(totalPayment, paymentScheduleElementList.get(2).getTotalPayment());
//        assertEquals(BigDecimal.valueOf(863.61), paymentScheduleElementList.get(2).getInterestPayment());
//        assertEquals(BigDecimal.valueOf(25186.67), paymentScheduleElementList.get(2).getDebtPayment());
//        assertEquals(BigDecimal.valueOf(25654.93), paymentScheduleElementList.get(2).getRemainingDebt());
//
//        assertEquals(totalPayment,paymentScheduleElementList.get(3).getTotalPayment());
//        assertEquals(BigDecimal.valueOf(421.72), paymentScheduleElementList.get(3).getInterestPayment());
//        assertEquals(BigDecimal.valueOf(25628.56), paymentScheduleElementList.get(3).getDebtPayment());
//        assertEquals(BigDecimal.valueOf(26.37), paymentScheduleElementList.get(3).getRemainingDebt());
//
//        assertEquals(BigDecimal.valueOf(26.37), paymentScheduleElementList.get(4).getTotalPayment());
//        assertEquals(BigDecimal.valueOf(0), paymentScheduleElementList.get(4).getInterestPayment());
//        assertEquals(BigDecimal.valueOf(26.37), paymentScheduleElementList.get(4).getDebtPayment());
//        assertEquals(BigDecimal.valueOf(0), paymentScheduleElementList.get(4).getRemainingDebt());
//
//    }

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
        assertEquals(BigDecimal.valueOf(968.94), creditParametersCalculationManager.calculateTotalAmountFromPaymentSchedule(paymentScheduleElementList));
    }
}