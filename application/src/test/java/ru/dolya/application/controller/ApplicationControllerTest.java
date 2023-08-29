package ru.dolya.application.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.service.ApplicationService;
import ru.dolya.application.service.OfferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ApplicationController.class)
class ApplicationControllerTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private MockMvc mockMvc;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean

    private ApplicationService applicationService;

    @MockBean
    private OfferService offerService;

    @Test
    void postOffersRequest() throws Exception {

        List<LoanOfferDTO> loanOffers = new ArrayList<>();
        loanOffers.add(LoanOfferDTO.builder()
                .requestedAmount(BigDecimal.valueOf(100_000))
                .totalAmount(BigDecimal.valueOf(122_149.92))
                .term(24)
                .monthlyPayment(BigDecimal.valueOf(5089.58))
                .rate(BigDecimal.valueOf(20.00))
                .isInsuranceEnabled(false)
                .isSalaryClient(false)
                .build());

        when(applicationService.getOffers(any())).thenReturn(loanOffers);

        mockMvc.perform(post("/application")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LoanApplicationRequestDTO.builder().build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].requestedAmount").value(100_000))
                .andExpect(jsonPath("$[0].totalAmount").value(122_149.92))
                .andExpect(jsonPath("$[0].term").value(24))
                .andExpect(jsonPath("$[0].monthlyPayment").value(5089.58))
                .andExpect(jsonPath("$[0].rate").value(20.00))
                .andExpect(jsonPath("$[0].isInsuranceEnabled").value(false))
                .andExpect(jsonPath("$[0].isSalaryClient").value(false));
    }

    @Test
    void putOffer() throws Exception {
        mockMvc.perform(put("/application/offer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LoanOfferDTO.builder().build())))
                .andExpect(status().isOk());
    }
}