package ru.dolya.deal.mapper;


import org.springframework.stereotype.Component;
import ru.dolya.deal.model.domain.Credit;
import ru.dolya.deal.model.dto.LoanOfferDTO;

@Component
public class CreditMapper {
    public Credit getCreditFromRequestDTO(LoanOfferDTO loanOfferDTO) {
        return Credit.builder()
                .amount(loanOfferDTO.getRequestedAmount())
                .term(loanOfferDTO.getTerm())
                .insuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .salaryClient(loanOfferDTO.getIsSalaryClient())
                .build();

    }
}
