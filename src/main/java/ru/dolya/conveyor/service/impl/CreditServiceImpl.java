package ru.dolya.conveyor.service.impl;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.conveyor.dto.CreditDTO;
import ru.dolya.conveyor.dto.PaymentScheduleElement;
import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.manager.impl.CreditParametersCalculationManagerImpl;
import ru.dolya.conveyor.manager.CreditRulesManager;
import ru.dolya.conveyor.manager.ScoringManager;
import ru.dolya.conveyor.service.CreditService;

import java.math.BigDecimal;
import java.util.List;
@Log4j2
@Service
public class CreditServiceImpl implements CreditService {
    private final ScoringManager scoringManager;
    private final CreditRulesManager creditRulesManager;
    private final CreditParametersCalculationManagerImpl creditParametersCalculationService;

    public CreditServiceImpl(ScoringManager scoringManager, CreditRulesManager creditRulesManager, CreditParametersCalculationManagerImpl creditParametersCalculationService) {
        this.scoringManager = scoringManager;
        this.creditRulesManager = creditRulesManager;
        this.creditParametersCalculationService = creditParametersCalculationService;
    }

    @Override
    public CreditDTO getCredit(ScoringDataDTO scoringDataDTO) {
        log.info("Request for calculate credit: {} {} - amount = {}, term = {}",
                scoringDataDTO.getFirstName(),
                scoringDataDTO.getLastName(),
                scoringDataDTO.getAmount(),
                scoringDataDTO.getTerm());

        BigDecimal rate = scoringManager.calculateRate(scoringDataDTO);

        if (scoringDataDTO.getIsInsuranceEnabled()){
            rate = rate.subtract(creditRulesManager.getInsuranceDiscount());
        }
        if (scoringDataDTO.getIsSalaryClient()){
            rate = rate.subtract(creditRulesManager.getSalaryClientDiscount());
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
