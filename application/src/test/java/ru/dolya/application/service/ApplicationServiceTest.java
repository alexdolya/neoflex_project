package ru.dolya.application.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.application.Application;
import ru.dolya.application.client.DealApi;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.exception.FeignClientCustomException;
import ru.dolya.application.manager.PreScoringManager;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class ApplicationServiceTest {
    @MockBean
    private PreScoringManager preScoringManager;

    @MockBean
    private DealApi dealApi;

    @Autowired
    private ApplicationService applicationService;

    @Test
    public void getOffers() {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder().build();
        when(preScoringManager.preScoring(requestDTO)).thenReturn(true);
        List<LoanOfferDTO> expectedOffers = new ArrayList<>();
        when(dealApi.postOffersRequest(requestDTO)).thenReturn(expectedOffers);
        List<LoanOfferDTO> actualOffers = applicationService.getOffers(requestDTO);
        assertEquals(expectedOffers, actualOffers);
    }

    @Test
    public void getOffersWithFeignException() {
        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder().build();
        when(preScoringManager.preScoring(requestDTO)).thenReturn(true);
        when(dealApi.postOffersRequest(requestDTO)).thenThrow(new FeignClientCustomException(new Throwable("Expected exception")));
        assertThrows(FeignClientCustomException.class, () -> applicationService.getOffers(requestDTO));
    }
}
