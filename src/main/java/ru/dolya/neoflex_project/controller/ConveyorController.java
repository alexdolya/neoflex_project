package ru.dolya.neoflex_project.controller;

import lombok.extern.log4j.Log4j2;
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

@Log4j2
@RestController
//@Tag(name = "Credit conveyor")
public class ConveyorController {
    private final OfferService offerService;
    private final CreditService creditService;


    public ConveyorController(OfferService offerService, CreditService creditService) {
        this.offerService = offerService;

        this.creditService = creditService;
    }

//    @Operation(summary = "Calculate 4 credit offers based on prescoring")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/conveyor/offers")
    public List<LoanOfferDTO> getOffers(@RequestBody LoanApplicationRequestDTO requestDTO) {
        log.info("Request credit offer: {} {} - amount = {}, term = {}, email = {}",
                requestDTO.getFirstName(),
                requestDTO.getLastName(),
                requestDTO.getAmount(),
                requestDTO.getTerm(),
                requestDTO.getEmail());
        return offerService.getOffers(requestDTO);
    }


//    @Operation(summary = "Calculate final credit offer")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/conveyor/calculation")
    public CreditDTO calculate(@RequestBody ScoringDataDTO scoringDataDTO) {
        log.info("Request for calculate credit: {} {} - amount = {}, term = {}",
                scoringDataDTO.getFirstName(),
                scoringDataDTO.getLastName(),
                scoringDataDTO.getAmount(),
                scoringDataDTO.getTerm());
        return creditService.getCredit(scoringDataDTO);
    }


}

