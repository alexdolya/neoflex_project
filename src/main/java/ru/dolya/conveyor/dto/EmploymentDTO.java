package ru.dolya.conveyor.dto;


import lombok.Builder;
import lombok.Data;
import ru.dolya.conveyor.dto.enums.EmploymentStatus;
import ru.dolya.conveyor.dto.enums.Position;

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
