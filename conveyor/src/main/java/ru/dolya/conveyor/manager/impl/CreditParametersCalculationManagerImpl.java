package ru.dolya.conveyor.manager.impl;

import ru.dolya.conveyor.manager.CreditParametersCalculationManager;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import ru.dolya.conveyor.dto.PaymentScheduleElement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Component
public class CreditParametersCalculationManagerImpl implements CreditParametersCalculationManager {

    @Override
    public BigDecimal calculateAnnuityPayment(BigDecimal requestedAmount, BigDecimal rate, int term) {

        BigDecimal monthlyRate = (rate.divide(BigDecimal.valueOf(12), 10, RoundingMode.HALF_UP)).divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        BigDecimal factor = (monthlyRate.add(BigDecimal.ONE)).pow(term).setScale(10, RoundingMode.HALF_UP);

        BigDecimal annuityPaymentCoefficient = ((monthlyRate.multiply(factor)).divide(factor.subtract(BigDecimal.ONE), 10, RoundingMode.HALF_UP));

        BigDecimal annuityPayment = requestedAmount.multiply(annuityPaymentCoefficient).setScale(2, RoundingMode.HALF_UP);

        log.debug("Annuity Payment: {} ", annuityPayment);
        return annuityPayment;
    }

    @Override
    public BigDecimal calculateTotalAmount(BigDecimal annuityPayment, int term) {
        BigDecimal totalAmount = annuityPayment.multiply(BigDecimal.valueOf(term));
        log.debug("TotalAmount: {} ", totalAmount);
        return totalAmount;
    }

    @Override
    public BigDecimal calculateTotalAmountFromPaymentSchedule(List<PaymentScheduleElement> paymentScheduleElementList) {
        BigDecimal totalAmountFromPayments = paymentScheduleElementList.stream()
                .map(PaymentScheduleElement::getTotalPayment)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        log.debug("TotalAmount: {} ", totalAmountFromPayments);
        return totalAmountFromPayments;
    }


    @Override
    public List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal annuityPayment, BigDecimal totalAmount, BigDecimal rate, int term) {
        List<PaymentScheduleElement> paymentScheduleElementList = new ArrayList<>();

        BigDecimal rateCoefficient = rate.divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);
        BigDecimal remainingDebt = totalAmount;
        LocalDate currentMonth = null;

        for (int i = 1; i <= term; i++) {


            currentMonth = LocalDate.now().plusMonths(i - 1);

            BigDecimal interestPayment = (remainingDebt.multiply(rateCoefficient).multiply(BigDecimal.valueOf(currentMonth.lengthOfMonth())))
                    .divide(BigDecimal.valueOf(currentMonth.lengthOfYear()), 2, RoundingMode.HALF_UP);

            BigDecimal debtPayment = annuityPayment.subtract(interestPayment);

            remainingDebt = remainingDebt.subtract(debtPayment);

            BigDecimal totalPayment = debtPayment.add(interestPayment);

            paymentScheduleElementList.add(PaymentScheduleElement.builder()
                    .number(i)
                    .date(currentMonth)
                    .totalPayment(totalPayment)
                    .interestPayment(interestPayment)
                    .debtPayment(debtPayment)
                    .remainingDebt(remainingDebt)
                    .build());
            log.debug("Number of payment: {}, interest payment: {}, debt payment: {}, remaining debt: {}", i, interestPayment, debtPayment, remainingDebt);
        }

        if (remainingDebt.compareTo(BigDecimal.ZERO) > 0) {
            paymentScheduleElementList.add(PaymentScheduleElement.builder()
                    .number(paymentScheduleElementList.size() + 1)
                    .date(currentMonth.plusMonths(1))
                    .totalPayment(remainingDebt)
                    .interestPayment(BigDecimal.ZERO)
                    .debtPayment(remainingDebt)
                    .remainingDebt(BigDecimal.ZERO)
                    .build());
            log.debug("Number of payment: {}, interest payment: {}, debt payment: {}, remaining debt: {}", paymentScheduleElementList.size() + 1, 0, remainingDebt, 0);
        }
        return paymentScheduleElementList;

    }
}
