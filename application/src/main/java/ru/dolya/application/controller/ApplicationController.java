package ru.dolya.application.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.service.ApplicationService;
import ru.dolya.application.service.OfferService;

import java.util.List;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;
    private final OfferService offerService;

    @Operation(summary = "Pre-score input data and returning 4 loan offers")    @ResponseStatus(HttpStatus.OK)
    @PostMapping()
    public List<LoanOfferDTO> postOffersRequest(@RequestBody LoanApplicationRequestDTO requestDTO) {
        return applicationService.getOffers(requestDTO);
    }

    @Operation(summary = "Accept one of the 4 offers and send it to the MS Deal")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/offer")
    public void putOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        offerService.putOffer(loanOfferDTO);
    }
}
