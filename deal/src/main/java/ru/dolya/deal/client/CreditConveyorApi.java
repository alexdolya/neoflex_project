package ru.dolya.deal.client;

import org.springframework.cloud.openfeign.FeignClient;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.deal.model.dto.CreditDTO;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.model.dto.ScoringDataDTO;

import java.util.List;

@FeignClient(name = "conveyorCalculation", url = "${feign.client.server}")
public interface CreditConveyorApi {

    @PostMapping("/calculation")
    CreditDTO calculate(@RequestBody ScoringDataDTO scoringDataDTO);

    @PostMapping("/offers")
    List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO);
}

