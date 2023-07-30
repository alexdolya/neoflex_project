package ru.dolya.neoflex_project.service;

import ru.dolya.neoflex_project.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.util.List;

public interface CreditParametersCalculation {
    BigDecimal calculateAnnuityPayment(BigDecimal requestedAmount, BigDecimal rate, int term);

    BigDecimal calculateTotalAmount(BigDecimal annuityPayment, int term);

    BigDecimal calculateTotalAmountFromPaymentSchedule(List<PaymentScheduleElement> paymentScheduleElementList);

    List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal annuityPayment, BigDecimal totalAmount, BigDecimal rate, int term);
}
