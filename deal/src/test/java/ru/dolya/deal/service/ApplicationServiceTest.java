package ru.dolya.deal.service;

import org.junit.jupiter.api.BeforeEach;
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
import ru.dolya.deal.model.domain.Client;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.repository.ClientRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DealApplication.class)
class ApplicationServiceTest {

    @Mock
    private ClientMapper clientMapper;

    @MockBean
    private ClientRepository clientRepository;

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private CreditConveyorApi creditConveyorApi;

    @Autowired
    private ApplicationService applicationService;

    private LoanApplicationRequestDTO requestDTO;

    private List<LoanOfferDTO> loanOfferDTOList;

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

        loanOfferDTOList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            loanOfferDTOList.add(LoanOfferDTO.builder().build());
        }


    }

    @Test
    void getOffers() {

        when(clientMapper.getClientFromRequestDTO(requestDTO)).thenReturn(Client.builder().build());
        when(clientRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(applicationRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        when(creditConveyorApi.getOffers(requestDTO)).thenReturn(loanOfferDTOList);

        List<LoanOfferDTO> loanOfferDTOList = applicationService.getOffers(requestDTO);

        assertEquals(4, loanOfferDTOList.size());

        verify(clientRepository, times(1)).save(any());
        verify(applicationRepository, times(1)).save(any());
    }
}



