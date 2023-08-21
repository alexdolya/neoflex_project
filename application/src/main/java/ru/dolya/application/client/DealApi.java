package ru.dolya.application.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "deal", url = "${feign.client.server}")
public interface DealApi {

    @PostMapping("/application")
    List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO);

    @PutMapping("/offer")
    void getOffer(@RequestBody LoanOfferDTO loanOfferDTO);
}


