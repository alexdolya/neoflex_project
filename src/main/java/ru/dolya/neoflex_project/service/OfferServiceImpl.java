package ru.dolya.neoflex_project.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


@Log4j2
@Service
public class OfferServiceImpl implements OfferService {

    private final PreScoringService preScoringService;
    private final CreditRules creditRules;

    private final CreditParametersCalculationService creditParametersCalculationService;

    public OfferServiceImpl(PreScoringService preScoringService, CreditRules creditRules, CreditParametersCalculationService creditParametersCalculationService) {
        this.preScoringService = preScoringService;
        this.creditRules = creditRules;
        this.creditParametersCalculationService = creditParametersCalculationService;
    }

    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {
        List<LoanOfferDTO> loanOfferDTOList = new ArrayList<>();

        if (preScoringService.preScoring(requestDTO)) {
            loanOfferDTOList.add(getOffer(false, false, requestDTO));
            loanOfferDTOList.add(getOffer(false, true, requestDTO));
            loanOfferDTOList.add(getOffer(true, false, requestDTO));
            loanOfferDTOList.add(getOffer(true, true, requestDTO));
        }

        log.info("4 loan offers successfully generated");
        return loanOfferDTOList;
    }

    public LoanOfferDTO getOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO requestDTO) {

        BigDecimal requestedAmount = requestDTO.getAmount();
        BigDecimal rate = creditRules.getBaseRate();


        if (isInsuranceEnabled) {
            rate = rate.subtract(creditRules.getInsuranceDiscount());
            requestedAmount = requestedAmount.add(creditRules.getInsuranceAmount());
        }

        if (isSalaryClient) {
            rate = rate.subtract(creditRules.getSalaryClientDiscount());
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
