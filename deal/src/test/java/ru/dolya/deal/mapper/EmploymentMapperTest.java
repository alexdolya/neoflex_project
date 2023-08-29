package ru.dolya.deal.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dolya.deal.mapper.EmploymentMapper;
import ru.dolya.deal.model.domain.Employment;
import ru.dolya.deal.model.domain.enums.EmploymentPosition;
import ru.dolya.deal.model.domain.enums.EmploymentStatus;
import ru.dolya.deal.model.domain.enums.Gender;
import ru.dolya.deal.model.domain.enums.MartialStatus;
import ru.dolya.deal.model.dto.EmploymentDTO;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EmploymentMapperTest {

    @Test
    public void testGetEmploymentFromRequestDTO() {

        FinishRegistrationRequestDTO requestDTO = FinishRegistrationRequestDTO.builder()
                .employment(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .employerINN("1234567890")
                        .salary(BigDecimal.valueOf(50000))
                        .position(EmploymentPosition.WORKER)
                        .workExperienceTotal(36)
                        .workExperienceCurrent(12)
                        .build())
                .build();


        EmploymentMapper employmentMapper = new EmploymentMapper();


        Employment employment = employmentMapper.getEmploymentFromRequestDTO(requestDTO);


        assertEquals(requestDTO.getEmployment().getEmploymentStatus(), employment.getEmployment().getEmploymentStatus());
        assertEquals(requestDTO.getEmployment().getEmployerINN(), employment.getEmployment().getEmployerINN());
        assertEquals(requestDTO.getEmployment().getSalary(), employment.getEmployment().getSalary());
        assertEquals(requestDTO.getEmployment().getPosition(), employment.getEmployment().getPosition());
        assertEquals(requestDTO.getEmployment().getWorkExperienceTotal(), employment.getEmployment().getWorkExperienceTotal());
        assertEquals(requestDTO.getEmployment().getWorkExperienceCurrent(), employment.getEmployment().getWorkExperienceCurrent());
    }
}