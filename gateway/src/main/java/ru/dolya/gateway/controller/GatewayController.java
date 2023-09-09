package ru.dolya.gateway.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.dolya.gateway.client.ApplicationApi;
import ru.dolya.gateway.client.DealApi;
import ru.dolya.gateway.dto.FinishRegistrationRequestDTO;
import ru.dolya.gateway.dto.LoanApplicationRequestDTO;
import ru.dolya.gateway.dto.LoanOfferDTO;
import ru.dolya.gateway.exception.FeignClientCustomException;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class GatewayController {
    private final ApplicationApi applicationApi;
    private final DealApi dealApi;

    @Operation(summary = "Create loan application")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/application")
    public List<LoanOfferDTO> createLoanApplication(@RequestBody LoanApplicationRequestDTO requestDTO) {
        try{
            return applicationApi.postOffersRequest(requestDTO);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }

    @Operation(summary = "Choose 1 of 4 offers")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/application/apply")
    public void applyOffer(@RequestBody LoanOfferDTO loanOfferDTO) {
        try{
            applicationApi.putOffer(loanOfferDTO);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }

    @Operation(summary = "Finish registration")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/application/registration/{applicationId}")
    public void finishRegistration(@PathVariable Long applicationId, @RequestBody FinishRegistrationRequestDTO requestDTO) {
        try{
            dealApi.calculateById(applicationId, requestDTO);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }

    @Operation(summary = "Create documents request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/document/{applicationId}")
    public void createDocuments(@PathVariable Long applicationId) {
        try{
            dealApi.send(applicationId);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }

    @Operation(summary = "Sign documents request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/document/{applicationId}/sign")
    public void signDocuments(@PathVariable Long applicationId) {
        try{
            dealApi.sign(applicationId);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }

    @Operation(summary = "Verify SES code request")
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/document/{applicationId}/code")
    public void verifySes(@PathVariable Long applicationId, @RequestBody Integer sesCode) {
        try{
            dealApi.verifySes(applicationId, sesCode);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }
    }


}
