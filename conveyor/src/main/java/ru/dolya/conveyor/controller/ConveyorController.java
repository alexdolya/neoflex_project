package ru.dolya.conveyor.controller;

import ru.dolya.conveyor.dto.ScoringDataDTO;
import ru.dolya.conveyor.service.CreditService;
import ru.dolya.conveyor.service.OfferService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.dolya.conveyor.dto.CreditDTO;
import ru.dolya.conveyor.dto.LoanApplicationRequestDTO;
import ru.dolya.conveyor.dto.LoanOfferDTO;

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
        return creditService.getCredit(scoringDataDTO);
    }


}

