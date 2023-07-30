package ru.dolya.neoflex_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dolya.neoflex_project.dto.CreditDTO;
import ru.dolya.neoflex_project.dto.LoanApplicationRequestDTO;
import ru.dolya.neoflex_project.dto.LoanOfferDTO;
import ru.dolya.neoflex_project.dto.ScoringDataDTO;
import ru.dolya.neoflex_project.service.CreditService;
import ru.dolya.neoflex_project.service.OfferService;

import java.util.List;

@RestController
public class ConveyorController {
    private final OfferService offerService;
    private final CreditService creditService;


    public ConveyorController(OfferService offerService, CreditService creditService) {
        this.offerService = offerService;

        this.creditService = creditService;
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/conveyor/offers")
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO) {
        return offerService.getOffers(requestDTO);
    }


    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/conveyor/calculation")
    public CreditDTO calculate(@RequestBody ScoringDataDTO scoringDataDTO) {
        return creditService.getCredit(scoringDataDTO);
    }


}

