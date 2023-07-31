package ru.dolya.neoflex_project.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.neoflex_project.dto.CreditDTO;
import ru.dolya.neoflex_project.dto.PaymentScheduleElement;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.util.List;
@Log4j2
@Service
public class CreditServiceImpl implements CreditService {
    private final ScoringService scoringService;
    private final CreditRules creditRules;
    private final CreditParametersCalculationService creditParametersCalculationService;

    public CreditServiceImpl(ScoringService scoringService, CreditRules creditRules, CreditParametersCalculationService creditParametersCalculationService) {
        this.scoringService = scoringService;
        this.creditRules = creditRules;
        this.creditParametersCalculationService = creditParametersCalculationService;
    }

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringDataDTO) {
        BigDecimal rate = scoringService.calculateRate(scoringDataDTO);

        if (scoringDataDTO.getIsInsuranceEnabled()){
            rate = rate.subtract(creditRules.getInsuranceDiscount());
        }
        if (scoringDataDTO.getIsSalaryClient()){
            rate = rate.subtract(creditRules.getSalaryClientDiscount());
        }

        BigDecimal monthlyPayment = creditParametersCalculationService.calculateAnnuityPayment(scoringDataDTO.getAmount(), rate, scoringDataDTO.getTerm());
        List<PaymentScheduleElement> paymentScheduleElementList = creditParametersCalculationService.calculatePaymentSchedule(monthlyPayment, scoringDataDTO.getAmount(), rate, scoringDataDTO.getTerm());
        BigDecimal psk = creditParametersCalculationService.calculateTotalAmountFromPaymentSchedule(paymentScheduleElementList);

        log.info("The loan is formed: PSK = {}, Monthly payment = {}, Rate = {}", psk, monthlyPayment, rate);

        return CreditDTO.builder()
                .amount(scoringDataDTO.getAmount())
                .term(scoringDataDTO.getTerm())
                .monthlyPayment(monthlyPayment)
                .rate(rate)
                .psk(psk)
                .isInsuranceEnabled(scoringDataDTO.getIsInsuranceEnabled())
                .isSalaryClient(scoringDataDTO.getIsSalaryClient())
                .paymentSchedule(paymentScheduleElementList)
                .build();


    }
}
