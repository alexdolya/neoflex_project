package ru.dolya.deal.mapper;

import org.springframework.stereotype.Component;
import ru.dolya.deal.model.domain.Client;
import ru.dolya.deal.model.domain.Passport;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.PassportDTO;

@Component
public class ClientMapper {

    public Client getClientFromRequestDTO(LoanApplicationRequestDTO requestDTO) {

        PassportDTO passportDTO = PassportDTO.builder()
                .number(requestDTO.getPassportNumber())
                .series(requestDTO.getPassportSeries())
                .build();

        return Client.builder()
                .firstName(requestDTO.getFirstName())
                .middleName(requestDTO.getMiddleName())
                .lastName(requestDTO.getLastName())
                .birthDate(requestDTO.getBirthdate())
                .email(requestDTO.getEmail())
                .passport(Passport.builder()
                        .passportDTO(passportDTO)
                        .build())
                .build();
    }


}
