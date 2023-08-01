package ru.dolya.conveyor.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.dolya.conveyor.dto.CreditDTO;
import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;
import ru.dolya.conveyor.dto.LoanOfferDTO;
import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.service.CreditService;
import ru.dolya.conveyor.service.OfferService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ConveyorController.class)
public class ConveyorControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OfferService offerService;
    @MockBean
    private CreditService creditService;

    @Test
    public void getOffers() throws Exception {

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

        when(offerService.getOffers(any())).thenReturn(loanOffers);

        mockMvc.perform(post("/conveyor/offers")
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
    public void calculate() throws Exception {

        when(creditService.getCredit(any())).thenReturn(CreditDTO.builder()
                .amount(BigDecimal.valueOf(100_000))
                .term(24)
                .monthlyPayment(BigDecimal.valueOf(4754.18))
                .rate(BigDecimal.valueOf(13))
                .psk(BigDecimal.valueOf(114115.45))
                .isInsuranceEnabled(false)
                .isSalaryClient(true)
                .build());

        mockMvc.perform(post("/conveyor/calculation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ScoringDataDTO.builder().build())))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.amount").value(100_000))
                .andExpect(jsonPath("$.term").value(24))
                .andExpect(jsonPath("$.monthlyPayment").value(4754.18))
                .andExpect(jsonPath("$.rate").value(13))
                .andExpect(jsonPath("$.psk").value(114115.45))
                .andExpect(jsonPath("$.isInsuranceEnabled").value(false))
                .andExpect(jsonPath("$.isSalaryClient").value(true));
    }
}