package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.client.CreditConveyorCalculateApi;
import ru.dolya.deal.domain.*;
import ru.dolya.deal.domain.enums.ApplicationStatus;
import ru.dolya.deal.domain.enums.ChangeType;
import ru.dolya.deal.domain.enums.CreditStatus;
import ru.dolya.deal.dto.CreditDTO;
import ru.dolya.deal.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.dto.ScoringDataDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.CalculateByIdService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalculateByIdServiceImpl implements CalculateByIdService {

    private final ApplicationRepository applicationRepository;
    private final CreditConveyorCalculateApi creditConveyorCalculateApi;

    @Transactional
    @Override
    public void calculateById(FinishRegistrationRequestDTO requestDTO, Long applicationId) {

        Application application = applicationRepository.findById(applicationId).orElseThrow();

        Client client = application.getClient();

        Employment employment = Employment.builder()
                .status(requestDTO.getEmployment().getEmploymentStatus())
                .employerInn(requestDTO.getEmployment().getEmployerINN())
                .salary(requestDTO.getEmployment().getSalary())
                .position(requestDTO.getEmployment().getPosition())
                .workExperienceTotal(requestDTO.getEmployment().getWorkExperienceTotal())
                .workExperienceCurrent(requestDTO.getEmployment().getWorkExperienceCurrent())
                .build();

        ScoringDataDTO scoringDataDTO = ScoringDataDTO.builder()
                .amount(application.getCredit().getAmount())
                .term(application.getCredit().getTerm())
                .firstName(client.getFirstName())
                .lastName(client.getLastName())
                .middleName(client.getMiddleName())
                .gender(requestDTO.getGender())
                .birthdate(client.getBirthDate())
                .passportSeries(client.getPassport().getSeries())
                .passportNumber(client.getPassport().getNumber())
                .passportIssueDate(requestDTO.getPassportIssueDate())
                .passportIssueBranch(requestDTO.getPassportIssueBranch())
                .martialStatus(requestDTO.getMartialStatus())
                .dependentAmount(requestDTO.getDependentAmount())
                .employment(requestDTO.getEmployment())
                .account(requestDTO.getAccount())
                .isInsuranceEnabled(application.getCredit().isInsuranceEnabled())
                .isSalaryClient(application.getCredit().isSalaryClient())
                .build();


        CreditDTO creditDTO = creditConveyorCalculateApi.calculate(scoringDataDTO);

        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .changeType(ChangeType.AUTOMATIC)
                .status(ApplicationStatus.CC_APPROVED)
                .time(LocalDateTime.now())
                .build());

        application.getCredit().setAmount(creditDTO.getAmount());
        application.getCredit().setTerm(creditDTO.getTerm());
        application.getCredit().setMonthlyPayment(creditDTO.getMonthlyPayment());
        application.getCredit().setRate(creditDTO.getRate());
        application.getCredit().setPsk(creditDTO.getPsk());
        application.getCredit().setCreditStatus(CreditStatus.CALCULATED);
        application.getCredit().setInsuranceEnabled(creditDTO.getIsInsuranceEnabled());
        application.getCredit().setSalaryClient(creditDTO.getIsSalaryClient());
        application.getCredit().setPaymentScheduleElementList(creditDTO.getPaymentSchedule());
        application.getClient().setGender(requestDTO.getGender());
        application.getClient().setMartialStatus(requestDTO.getMartialStatus());
        application.getClient().setDependentAmount(requestDTO.getDependentAmount());
        application.getClient().setAccount(requestDTO.getAccount());
        application.getClient().setEmployment(employment);
        application.getClient().getPassport().setIssueBranch(requestDTO.getPassportIssueBranch());
        application.getClient().getPassport().setIssueDate(requestDTO.getPassportIssueDate());
        application.setStatus(ApplicationStatus.CC_APPROVED);
        application.setStatusHistory(statusHistoryList);

        applicationRepository.save(application);
    }
}
