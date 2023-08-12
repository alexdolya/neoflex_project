package ru.dolya.deal.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dolya.deal.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.dto.LoanOfferDTO;
import ru.dolya.deal.service.ApplicationService;
import ru.dolya.deal.service.CalculateByIdService;
import ru.dolya.deal.service.OfferService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class DealController {

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateByIdService calculateByIdService;

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/deal/application")
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO) {
        return applicationService.getOffers(requestDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/deal/offer")
    public void getOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        offerService.getOffer(loanOfferDTO);
    }


    @PutMapping("/deal/calculate/{applicationId}")

    public void calculateById(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO requestDTO) {
        calculateByIdService.calculateById(requestDTO, applicationId);
    }

}

