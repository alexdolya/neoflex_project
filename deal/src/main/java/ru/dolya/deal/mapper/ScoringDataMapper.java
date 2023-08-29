package ru.dolya.deal.mapper;

import org.springframework.stereotype.Component;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.model.dto.ScoringDataDTO;
@Component
public class ScoringDataMapper {

    public ScoringDataDTO getScoringDataDTOFromApplication(Application application, FinishRegistrationRequestDTO requestDTO){
        return ScoringDataDTO.builder()
                .amount(application.getCredit().getAmount())
                .term(application.getCredit().getTerm())
                .firstName(application.getClient().getFirstName())
                .lastName(application.getClient().getLastName())
                .middleName(application.getClient().getMiddleName())
                .gender(requestDTO.getGender())
                .birthdate(application.getClient().getBirthDate())
                .passportSeries(application.getClient().getPassport().getPassportDTO().getSeries())
                .passportNumber(application.getClient().getPassport().getPassportDTO().getNumber())
                .passportIssueDate(requestDTO.getPassportIssueDate())
                .passportIssueBranch(requestDTO.getPassportIssueBranch())
                .martialStatus(requestDTO.getMartialStatus())
                .dependentAmount(requestDTO.getDependentAmount())
                .employment(requestDTO.getEmployment())
                .account(requestDTO.getAccount())
                .isInsuranceEnabled(application.getCredit().isInsuranceEnabled())
                .isSalaryClient(application.getCredit().isSalaryClient())
                .build();
    }


}
