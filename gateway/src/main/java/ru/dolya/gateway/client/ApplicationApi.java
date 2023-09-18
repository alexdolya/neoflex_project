package ru.dolya.gateway.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.gateway.dto.LoanApplicationRequestDTO;
import ru.dolya.gateway.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "application", url = "${feign.client.application}")
public interface ApplicationApi {
    @PostMapping()
    List<LoanOfferDTO> postOffersRequest(@RequestBody LoanApplicationRequestDTO requestDTO);

    @PutMapping("/offer")
    void putOffer(@RequestBody LoanOfferDTO loanOfferDTO);
}