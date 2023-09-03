package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.client.CreditConveyorApi;
import ru.dolya.deal.exception.FeignClientCustomException;
import ru.dolya.deal.mapper.EmploymentMapper;
import ru.dolya.deal.mapper.ScoringDataMapper;
import ru.dolya.deal.model.domain.*;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;
import ru.dolya.deal.model.domain.enums.CreditStatus;
import ru.dolya.deal.model.dto.*;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.CalculateByIdService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
public class CalculateByIdServiceImpl implements CalculateByIdService {

    private final ApplicationRepository applicationRepository;
    private final CreditConveyorApi creditConveyorApi;
    private final EmploymentMapper employmentMapper;
    private final ScoringDataMapper scoringDataMapper;
    private final KafkaProducerService kafkaProducerService;


    @Transactional
    @Override
    public void calculateById(FinishRegistrationRequestDTO requestDTO, Long applicationId) {

        Application application = applicationRepository.findById(applicationId).orElseThrow();

        log.info("Successful load application with ID: {} from database", applicationId);

        Employment employment = employmentMapper.getEmploymentFromRequestDTO(requestDTO);

        ScoringDataDTO scoringDataDTO = scoringDataMapper.getScoringDataDTOFromApplication(application, requestDTO);

        CreditDTO creditDTO;

        try {
            creditDTO = creditConveyorApi.calculate(scoringDataDTO);
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }

        log.info("calculated loan with the following parameters: amount = {}, PSK = {}, monthly payment = {}",
                creditDTO.getAmount(),
                creditDTO.getPsk(),
                creditDTO.getMonthlyPayment());

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
        application.getClient().getPassport().getPassportDTO().setIssueBranch(requestDTO.getPassportIssueBranch());
        application.getClient().getPassport().getPassportDTO().setIssueDate(requestDTO.getPassportIssueDate());
        application.setStatus(ApplicationStatus.CC_APPROVED);
        application.setStatusHistory(statusHistoryList);

        log.info("Set application status to CC_APPROVED");

        EmailMessage emailMessage = new EmailMessage()
                .setApplicationId(application.getApplicationId())
                .setTheme(Theme.CREATE_DOCUMENT)
                .setAddress(application.getClient().getEmail());

        applicationRepository.save(application);

        kafkaProducerService.send("create-documents", emailMessage);
        log.info("Send email message : {} to create-documents topic", emailMessage);

    }
}
