package ru.dolya.deal.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.service.*;

import java.util.List;

@RestController
@RequestMapping("/deal")
@RequiredArgsConstructor
public class DealController {
    private final ApplicationService applicationService;
    private final OfferService offerService;
    private final CalculateByIdService calculateByIdService;
    private final SendService sendService;
    private final StatusService statusService;
    private final SesService sesService;
    private final SignService signService;

    @Operation(summary = "Calculate 4 credit offers from MS Conveyor")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/application")
    public List<LoanOfferDTO> postOffersRequest(@RequestBody LoanApplicationRequestDTO requestDTO) {
        return applicationService.getOffers(requestDTO);
    }

    @Operation(summary = "Refresh application from LoanOfferDTO parameters and save to DB")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/offer")
    public void putOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        offerService.getOffer(loanOfferDTO);
    }

    @Operation(summary = "Refresh application and credit entities by FinishRegistrationRequestDTO and save them to DB")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/calculate/{applicationId}")
    public void calculateById(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO requestDTO) {
        calculateByIdService.calculateById(requestDTO, applicationId);
    }

    @Operation(summary = "Request to send documents")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/document/{applicationId}/send")
    public void send(@PathVariable Long applicationId) {
        sendService.send(applicationId);
    }

    @Operation(summary = "Signing of documents")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/document/{applicationId}/code")
    public void verifySes(@PathVariable Long applicationId, @RequestBody Integer sesCode) {
        sesService.verifySes(applicationId, sesCode);
    }

    @Operation(summary = "Set application status to DOCUMENT_CREATED")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/admin/application/{applicationId}/status")
    public void status(@PathVariable Long applicationId) {
        statusService.setStatus(applicationId);
    }

    @Operation(summary = "Set SES code generated by MS Dossier")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/admin/application/{applicationId}/ses")
    public void setSes(@PathVariable Long applicationId, @RequestBody Integer ses) {
        sesService.setSes(applicationId, ses);
    }

    @Operation(summary = "Request to sign documents")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/document/{applicationId}/sign")
    public void sign(@PathVariable Long applicationId) {
        signService.sign(applicationId);
    }

}

