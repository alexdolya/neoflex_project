package ru.dolya.conveyor.manager;

import ru.dolya.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

public interface CreditParametersCalculationManager {
    BigDecimal calculateAnnuityPayment(BigDecimal requestedAmount, BigDecimal rate, int term);

    BigDecimal calculateTotalAmount(BigDecimal annuityPayment, int term);

    BigDecimal calculateTotalAmountFromPaymentSchedule(List<PaymentScheduleElement> paymentScheduleElementList);

    List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal annuityPayment, BigDecimal totalAmount, BigDecimal rate, int term);
}
