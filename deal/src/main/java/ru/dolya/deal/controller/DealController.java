package ru.dolya.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.service.ApplicationService;
import ru.dolya.deal.service.CalculateByIdService;
import ru.dolya.deal.service.OfferService;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {

    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateByIdService calculateByIdService;

    @Operation(summary = "Calculate 4 credit offers from MS Conveyor")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/application")
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO) {
        return applicationService.getOffers(requestDTO);
    }

    @Operation(summary = "Refresh application from LoanOfferDTO parameters and save to DB")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/offer")
    public void getOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        offerService.getOffer(loanOfferDTO);
    }

    @Operation(summary = "Refresh application and credit entities by FinishRegistrationRequestDTO and save them to DB")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/calculate/{applicationId}")
    public void calculateById(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO requestDTO) {
        calculateByIdService.calculateById(requestDTO, applicationId);
    }

}

