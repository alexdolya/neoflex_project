package ru.dolya.conveyor.service.impl;

import lombok.RequiredArgsConstructor;
import ru.dolya.conveyor.manager.CreditRulesManager;
import ru.dolya.conveyor.manager.PreScoringManager;
import ru.dolya.conveyor.manager.impl.CreditParametersCalculationManagerImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;
import ru.dolya.conveyor.dto.LoanOfferDTO;
import ru.dolya.conveyor.service.OfferService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {


    private final CreditRulesManager creditRulesManager;
    private final CreditParametersCalculationManagerImpl creditParametersCalculationService;

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {
        log.info("Request credit offer: {} {} - amount = {}, term = {}, email = {}",
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getAmount(),
                requestDTO.getTerm(),
                requestDTO.getEmail());

        List<LoanOfferDTO> loanOfferDTOList = new ArrayList<>();

            loanOfferDTOList.add(getOffer(false, false, requestDTO));
            loanOfferDTOList.add(getOffer(false, true, requestDTO));
            loanOfferDTOList.add(getOffer(true, false, requestDTO));
            loanOfferDTOList.add(getOffer(true, true, requestDTO));


        log.info("4 loan offers successfully generated");
        return loanOfferDTOList;
    }

    public LoanOfferDTO getOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO requestDTO) {

        BigDecimal requestedAmount = requestDTO.getAmount();
        BigDecimal rate = creditRulesManager.getBaseRate();


        if (isInsuranceEnabled) {
            rate = rate.subtract(creditRulesManager.getInsuranceDiscount());
            requestedAmount = requestedAmount.add(creditRulesManager.getInsuranceAmount());
        }

        if (isSalaryClient) {
            rate = rate.subtract(creditRulesManager.getSalaryClientDiscount());
        }

        BigDecimal annuityPayment = creditParametersCalculationService.calculateAnnuityPayment(requestedAmount, rate, requestDTO.getTerm());

        BigDecimal totalAmount = creditParametersCalculationService.calculateTotalAmount(annuityPayment, requestDTO.getTerm());

        log.info("Loan offer successfully generated: annuity payment = {}, total amount = {}, rate = {}", annuityPayment, totalAmount, rate);

        return LoanOfferDTO.builder()
                .isInsuranceEnabled(isInsuranceEnabled)
                .isSalaryClient(isSalaryClient)
                .requestedAmount(requestDTO.getAmount())
                .totalAmount(totalAmount.setScale(2, RoundingMode.HALF_UP))
                .term(requestDTO.getTerm())
                .monthlyPayment(annuityPayment.setScale(2, RoundingMode.HALF_UP))
                .rate(rate.setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
