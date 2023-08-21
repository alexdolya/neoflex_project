package ru.dolya.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.application.client.DealApi;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.service.OfferService;

@Service
@Log4j2
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final DealApi dealApi;

    @Override
    public void putOffer(LoanOfferDTO loanOfferDTO) {
        dealApi.getOffer(loanOfferDTO);
        log.info("Loan offer with Id: {} approved", loanOfferDTO.getApplicationId());
    }
}
