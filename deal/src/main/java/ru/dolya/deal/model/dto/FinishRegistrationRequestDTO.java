package ru.dolya.deal.model.dto;

import lombok.Builder;
import lombok.Data;
import ru.dolya.deal.model.domain.enums.Gender;
import ru.dolya.deal.model.domain.enums.MartialStatus;

import java.time.LocalDate;

@Data
@Builder
public class FinishRegistrationRequestDTO {
    private Gender gender;
    private MartialStatus martialStatus;
    private Integer dependentAmount;
    private LocalDate passportIssueDate;
    private String passportIssueBranch;
    private EmploymentDTO employment;
    private String account;
}

