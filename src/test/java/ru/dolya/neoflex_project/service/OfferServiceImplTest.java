package ru.dolya.neoflex_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OfferServiceImplTest {

    @Mock
    private PreScoringService preScoringService;

    @Mock
    private CreditRules creditRules;

    @Mock
    private CreditParametersCalculationService parametersCalculation;

    @InjectMocks
    private OfferServiceImpl offerService;

    private LoanApplicationRequestDTO requestDTO;

    @BeforeEach
    void init() {
        requestDTO = LoanApplicationRequestDTO.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .birthdate(LocalDate.of(2015, 1, 1))
                .email("ivanov@mail.ru")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
    }

    @Test
    void getOffer() {
        when(creditRules.getBaseRate()).thenReturn(BigDecimal.valueOf(20));
        when(parametersCalculation.calculateAnnuityPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(5089.58));
        when(parametersCalculation.calculateTotalAmount(any(), anyInt())).thenReturn(BigDecimal.valueOf(122149.92));

        LoanOfferDTO loanOfferDTOActual = offerService.getOffer(false, false, requestDTO);

        assertEquals(BigDecimal.valueOf(122149.92), loanOfferDTOActual.getTotalAmount());
        assertEquals(BigDecimal.valueOf(5089.58), loanOfferDTOActual.getMonthlyPayment());
        assertEquals(BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_UP), loanOfferDTOActual.getRate());
    }

    @Test
    void getOffers() {
        when(preScoringService.preScoring(requestDTO)).thenReturn(true);
        when(creditRules.getBaseRate()).thenReturn(BigDecimal.valueOf(20));
        when(parametersCalculation.calculateTotalAmount(any(), anyInt())).thenReturn(BigDecimal.valueOf(122149.92));
        when(parametersCalculation.calculateAnnuityPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(5089.58));
        when(creditRules.getSalaryClientDiscount()).thenReturn(BigDecimal.valueOf(1));
        when(creditRules.getInsuranceAmount()).thenReturn(BigDecimal.valueOf(20000));
        when(creditRules.getInsuranceDiscount()).thenReturn(BigDecimal.valueOf(3));

        List<LoanOfferDTO> loanOfferDTOList = offerService.getOffers(requestDTO);
        assertEquals(4, loanOfferDTOList.size());
    }


}