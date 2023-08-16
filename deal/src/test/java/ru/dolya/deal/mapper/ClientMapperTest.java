package ru.dolya.deal.mapper;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;


import ru.dolya.deal.model.domain.Client;

import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;


import java.time.LocalDate;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class ClientMapperTest {

    @Test
    public void testGetClientFromRequestDTO() {

        LoanApplicationRequestDTO requestDTO = LoanApplicationRequestDTO.builder()
                .amount(BigDecimal.valueOf(1_000_000))
                .term(36)
                .firstName("Ivan")
                .lastName("Ivanovich")
                .middleName("Ivanov")
                .email("ivanov@mail.ru")
                .birthdate(LocalDate.of(1990, 1, 1))
                .passportSeries("1234")
                .passportNumber("567890")
                .build();


        ClientMapper clientMapper = new ClientMapper();


        Client client = clientMapper.getClientFromRequestDTO(requestDTO);


        assertEquals(requestDTO.getFirstName(), client.getFirstName());
        assertEquals(requestDTO.getMiddleName(), client.getMiddleName());
        assertEquals(requestDTO.getLastName(), client.getLastName());
        assertEquals(requestDTO.getEmail(), client.getEmail());
        assertEquals(requestDTO.getBirthdate(), client.getBirthDate());
        assertEquals(requestDTO.getPassportSeries(), client.getPassport().getPassportDTO().getSeries());
        assertEquals(requestDTO.getPassportNumber(), client.getPassport().getPassportDTO().getNumber());
    }
}