package ru.dolya.application.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.application.Application;
import ru.dolya.application.client.DealApi;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.service.impl.OfferServiceImpl;

import java.net.ConnectException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = Application.class)
class OfferServiceTest {

    @MockBean
    private DealApi dealApi;

    @Autowired
    private OfferService offerService;

    @Test
    void putOffer() {
        LoanOfferDTO loanOfferDTO = LoanOfferDTO.builder().build();
        doThrow(new RuntimeException("Error occurred while putting offer")).when(dealApi).putOffer(loanOfferDTO);
        assertThrows(RuntimeException.class, () -> offerService.putOffer(loanOfferDTO));
        verify(dealApi, times(1)).putOffer(loanOfferDTO);
    }
}