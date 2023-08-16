package ru.dolya.deal.mapper;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import ru.dolya.deal.model.domain.Employment;
import ru.dolya.deal.model.dto.EmploymentDTO;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;

@Component
public class EmploymentMapper {

    public Employment getEmploymentFromRequestDTO(FinishRegistrationRequestDTO requestDTO) {
        EmploymentDTO employmentDTO = EmploymentDTO.builder()
                .employmentStatus(requestDTO.getEmployment().getEmploymentStatus())
                .employerINN(requestDTO.getEmployment().getEmployerINN())
                .salary(requestDTO.getEmployment().getSalary())
                .position(requestDTO.getEmployment().getPosition())
                .workExperienceTotal(requestDTO.getEmployment().getWorkExperienceTotal())
                .workExperienceCurrent(requestDTO.getEmployment().getWorkExperienceCurrent())
                .build();

        return Employment.builder()
                .employment(employmentDTO)
                .build();
    }

}
