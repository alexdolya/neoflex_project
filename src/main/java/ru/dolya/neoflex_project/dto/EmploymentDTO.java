package ru.dolya.neoflex_project.dto;


import lombok.Builder;
import lombok.Data;
import ru.dolya.neoflex_project.dto.enums.EmploymentStatus;
import ru.dolya.neoflex_project.dto.enums.Position;

import java.math.BigDecimal;

@Data
@Builder
public class EmploymentDTO {
    private EmploymentStatus employmentStatus;
    private String employerINN;
    private BigDecimal salary;
    private Position position;
    private Integer workExperienceTotal;
    private Integer workExperienceCurrent;
}
