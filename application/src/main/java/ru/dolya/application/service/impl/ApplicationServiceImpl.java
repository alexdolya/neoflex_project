package ru.dolya.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.dolya.application.client.DealApi;
import ru.dolya.application.dto.LoanApplicationRequestDTO;
import ru.dolya.application.dto.LoanOfferDTO;
import ru.dolya.application.exception.FeignClientCustomException;
import ru.dolya.application.exception.PreScoringException;
import ru.dolya.application.manager.PreScoringManager;
import ru.dolya.application.service.ApplicationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final PreScoringManager preScoringManager;
    private final DealApi dealApi;

    @Override
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {
        if (preScoringManager.preScoring(requestDTO)) {
            List<LoanOfferDTO> loanOfferDTOList;
            try {
                loanOfferDTOList = dealApi.postOffersRequest(requestDTO);
            } catch (Exception ex) {
                throw new FeignClientCustomException(new Throwable(ex.getMessage()));
            }
            return loanOfferDTOList;
        } else throw new PreScoringException(new Throwable("The input data was not pre-scored"));
    }
}
