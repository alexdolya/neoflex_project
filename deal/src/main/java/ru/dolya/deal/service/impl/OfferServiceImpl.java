package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.domain.Application;
import ru.dolya.deal.domain.Credit;
import ru.dolya.deal.domain.StatusHistory;
import ru.dolya.deal.domain.enums.ApplicationStatus;
import ru.dolya.deal.domain.enums.ChangeType;
import ru.dolya.deal.dto.LoanOfferDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.OfferService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final ApplicationRepository applicationRepository;

    @Transactional
    @Override
    public void getOffer(LoanOfferDTO loanOfferDTO) {
        Long appId = loanOfferDTO.getApplicationId();
        Application application = applicationRepository.findById(appId).orElseThrow();
        List<StatusHistory> statusHistory = application.getStatusHistory();

        if (statusHistory == null) {
            statusHistory = new ArrayList<>();
        }

        statusHistory.add(StatusHistory.builder()
                .changeType(ChangeType.AUTOMATIC)
                .status(ApplicationStatus.APPROVED)
                .time(LocalDateTime.now())
                .build());

        Credit credit = Credit.builder()
                .amount(loanOfferDTO.getRequestedAmount())
                .term(loanOfferDTO.getTerm())
                .insuranceEnabled(loanOfferDTO.getIsInsuranceEnabled())
                .salaryClient(loanOfferDTO.getIsSalaryClient())
                .build();

        application.setStatus(ApplicationStatus.PREAPPROVAL);
        application.setStatusHistory(statusHistory);
        application.setCredit(credit);
        application.setAppliedOffer(loanOfferDTO);

        applicationRepository.save(application);

    }
}

