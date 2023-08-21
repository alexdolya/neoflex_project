package ru.dolya.application.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.application.Application;
import ru.dolya.application.client.DealApi;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.exception.PreScoringException;
import ru.dolya.application.manager.PreScoringManager;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class ApplicationServiceTest {
    @Mock
    private PreScoringManager preScoringManager;

    @Mock
    private DealApi dealApi;

    @Mock
    private ApplicationService applicationService;

    @Test
    public void getOffers() {

        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder().build();
        when(preScoringManager.preScoring(requestDTO)).thenReturn(true);

        List<LoanOfferDTO> expectedOffers = new ArrayList<>();

        when(dealApi.getOffers(requestDTO)).thenReturn(expectedOffers);

        List<LoanOfferDTO> actualOffers = applicationService.getOffers(requestDTO);

        assertEquals(expectedOffers, actualOffers);
    }
}
