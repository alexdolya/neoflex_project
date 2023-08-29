package ru.dolya.deal.mapper;

import org.junit.jupiter.api.Test;
import ru.dolya.deal.model.domain.Credit;
import ru.dolya.deal.model.dto.LoanOfferDTO;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CreditMapperTest {

    @Test
    public void testGetCreditFromRequestDTO() {

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(10000))
                .term(12)
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        CreditMapper creditMapper = new CreditMapper();

        Credit credit = creditMapper.getCreditFromRequestDTO(loanOfferDTO);

        assertEquals(loanOfferDTO.getRequestedAmount(), credit.getAmount());
        assertEquals(loanOfferDTO.getTerm(), credit.getTerm());
        assertEquals(loanOfferDTO.getIsInsuranceEnabled(), credit.isInsuranceEnabled());
        assertEquals(loanOfferDTO.getIsSalaryClient(), credit.isSalaryClient());
    }
}