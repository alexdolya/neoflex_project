package ru.dolya.gateway.dto;


import lombok.Builder;
import lombok.Data;
import ru.dolya.gateway.dto.enums.EmploymentPosition;
import ru.dolya.gateway.dto.enums.EmploymentStatus;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private EmploymentPosition position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
