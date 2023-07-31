package ru.dolya.neoflex_project.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Credit conveyor", description = "Calculate credit offers")
public class ConveyorController {
    private final OfferService offerService;
    private final CreditService creditService;


    public ConveyorController(OfferService offerService, CreditService creditService) {
        this.offerService = offerService;

        this.creditService = creditService;
    }

    @Operation(summary = "Calculate 4 credit offers based on prescoring")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Wrong input parameter")
    })
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


    @Operation(summary = "Calculate final credit offer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation"),
            @ApiResponse(responseCode = "500", description = "Wrong input parameter")
    })
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

