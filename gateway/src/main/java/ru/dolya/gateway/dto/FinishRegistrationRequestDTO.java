package ru.dolya.gateway.dto;

import lombok.Builder;
import lombok.Data;
import ru.dolya.gateway.dto.enums.Gender;
import ru.dolya.gateway.dto.enums.MartialStatus;

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

