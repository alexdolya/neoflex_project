package ru.dolya.conveyor.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.conveyor.ConveyorApplication;
import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;
import ru.dolya.conveyor.dto.LoanOfferDTO;
import ru.dolya.conveyor.manager.impl.CreditParametersCalculationManagerImpl;
import ru.dolya.conveyor.manager.CreditRulesManager;
import ru.dolya.conveyor.manager.PreScoringManager;
import ru.dolya.conveyor.service.impl.OfferServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConveyorApplication.class)
class OfferServiceTest {

    @Mock
    private PreScoringManager preScoringService;

    @Mock
    private CreditRulesManager creditRulesManager;

    @Mock
    private CreditParametersCalculationManagerImpl parametersCalculation;

    @Autowired
    private OfferService offerService;

    private LoanApplicationRequestDTO requestDTO;

    @BeforeEach
    void init() {
        requestDTO = LoanApplicationRequestDTO.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .amount(BigDecimal.valueOf(100000))
                .term(24)
                .birthdate(LocalDate.of(1990, 1, 1))
                .email("ivanov@mail.ru")
                .passportSeries("1234")
                .passportNumber("567890")
                .build();
    }

    @Test
    void getOffer() {
        when(creditRulesManager.getBaseRate()).thenReturn(BigDecimal.valueOf(20));
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
        when(creditRulesManager.getBaseRate()).thenReturn(BigDecimal.valueOf(20));
        when(parametersCalculation.calculateTotalAmount(any(), anyInt())).thenReturn(BigDecimal.valueOf(122149.92));
        when(parametersCalculation.calculateAnnuityPayment(any(), any(), anyInt())).thenReturn(BigDecimal.valueOf(5089.58));
        when(creditRulesManager.getSalaryClientDiscount()).thenReturn(BigDecimal.valueOf(1));
        when(creditRulesManager.getInsuranceAmount()).thenReturn(BigDecimal.valueOf(20000));
        when(creditRulesManager.getInsuranceDiscount()).thenReturn(BigDecimal.valueOf(3));

        List<LoanOfferDTO> loanOfferDTOList = offerService.getOffers(requestDTO);
        assertEquals(4, loanOfferDTOList.size());
    }


}