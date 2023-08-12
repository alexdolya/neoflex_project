package ru.dolya.deal.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.dolya.deal.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.dto.LoanOfferDTO;

import java.util.List;

@FeignClient(name = "conveyorOffers", url = "http://localhost:8080/conveyor/offers")
public interface CreditConveyorOffersApi {
      @PostMapping
      List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO);
}


