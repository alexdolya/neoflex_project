package ru.dolya.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.deal.DealApplication;
import ru.dolya.deal.client.CreditConveyorApi;
import ru.dolya.deal.mapper.ClientMapper;
import ru.dolya.deal.mapper.CreditMapper;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.Client;
import ru.dolya.deal.model.domain.Credit;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.repository.ClientRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DealApplication.class)
class OfferServiceTest {

    @MockBean
    private ApplicationRepository applicationRepository;

    @Mock
    private CreditMapper creditMapper;

    @Autowired
    private OfferService offerService;

    @Test
    void getOffer() {

        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(10000))
                .term(12)
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();


        when(applicationRepository.findById(any())).thenReturn(Optional.ofNullable(Application.builder().build()));
        when(creditMapper.getCreditFromRequestDTO(loanOfferDTO)).thenReturn(Credit.builder().build());
        when(applicationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);

        offerService.getOffer(loanOfferDTO);
        verify(applicationRepository, times(1)).findById(any());
        verify(applicationRepository, times(1)).save(any());
    }
}