package ru.dolya.deal.mapper;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.Client;
import ru.dolya.deal.model.domain.Credit;
import ru.dolya.deal.model.domain.Passport;
import ru.dolya.deal.model.domain.enums.EmploymentPosition;
import ru.dolya.deal.model.domain.enums.EmploymentStatus;
import ru.dolya.deal.model.domain.enums.Gender;
import ru.dolya.deal.model.domain.enums.MartialStatus;
import ru.dolya.deal.model.dto.EmploymentDTO;
import ru.dolya.deal.model.dto.FinishRegistrationRequestDTO;
import ru.dolya.deal.model.dto.PassportDTO;
import ru.dolya.deal.model.dto.ScoringDataDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ScoringDataMapperTest {

    @Test
    public void testGetScoringDataDTOFromApplication() {
        // Create a sample Application
        Application application = new Application();

        application.setCredit(Credit.builder()
                .amount(BigDecimal.valueOf(10000))
                .term(12)
                .insuranceEnabled(true)
                .salaryClient(false)
                .build());

        Client client = Client.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .birthDate(LocalDate.of(1990, 1, 1))
                .passport(Passport.builder()
                        .passportDTO(PassportDTO.builder()
                                .series("1234")
                                .number("567890")
                                .build())
                        .build())
                .build();

        application.setClient(client);


        FinishRegistrationRequestDTO requestDTO = FinishRegistrationRequestDTO.builder()
                .gender(Gender.MALE)
                .martialStatus(MartialStatus.SINGLE)
                .dependentAmount(1)
                .passportIssueDate(LocalDate.of(2010, 1, 1))
                .passportIssueBranch("UFMS")
                .employment(EmploymentDTO.builder()
                        .employmentStatus(EmploymentStatus.EMPLOYED)
                        .employerINN("1234567890")
                        .salary(BigDecimal.valueOf(50000))
                        .position(EmploymentPosition.WORKER)
                        .workExperienceTotal(36)
                        .workExperienceCurrent(12)
                        .build())
                .account("123456789")
                .build();


        ScoringDataMapper scoringDataMapper = new ScoringDataMapper();


        ScoringDataDTO scoringDataDTO = scoringDataMapper.getScoringDataDTOFromApplication(application, requestDTO);


        assertEquals(application.getCredit().getAmount(), scoringDataDTO.getAmount());
        assertEquals(application.getCredit().getTerm(), scoringDataDTO.getTerm());
        assertEquals(application.getClient().getFirstName(), scoringDataDTO.getFirstName());
        assertEquals(application.getClient().getLastName(), scoringDataDTO.getLastName());
        assertEquals(application.getClient().getMiddleName(), scoringDataDTO.getMiddleName());
        assertEquals(requestDTO.getGender(), scoringDataDTO.getGender());
        assertEquals(application.getClient().getBirthDate(), scoringDataDTO.getBirthdate());
        assertEquals(application.getClient().getPassport().getPassportDTO().getSeries(), scoringDataDTO.getPassportSeries());
        assertEquals(application.getClient().getPassport().getPassportDTO().getNumber(), scoringDataDTO.getPassportNumber());
        assertEquals(requestDTO.getPassportIssueDate(), scoringDataDTO.getPassportIssueDate());
        assertEquals(requestDTO.getPassportIssueBranch(), scoringDataDTO.getPassportIssueBranch());
        assertEquals(requestDTO.getMartialStatus(), scoringDataDTO.getMartialStatus());
        assertEquals(requestDTO.getDependentAmount(), scoringDataDTO.getDependentAmount());
        assertEquals(requestDTO.getEmployment(), scoringDataDTO.getEmployment());
        assertEquals(requestDTO.getAccount(), scoringDataDTO.getAccount());
        assertEquals(application.getCredit().isInsuranceEnabled(), scoringDataDTO.getIsInsuranceEnabled());
        assertEquals(application.getCredit().isSalaryClient(), scoringDataDTO.getIsSalaryClient());
    }
}