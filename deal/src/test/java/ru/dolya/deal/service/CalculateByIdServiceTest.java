package ru.dolya.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.deal.DealApplication;
import ru.dolya.deal.client.CreditConveyorApi;
import ru.dolya.deal.model.domain.*;
import ru.dolya.deal.model.domain.enums.*;
import ru.dolya.deal.model.dto.*;
import ru.dolya.deal.repository.ApplicationRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DealApplication.class)
class CalculateByIdServiceTest {

    @MockBean
    private ApplicationRepository applicationRepository;

    @MockBean
    private CreditConveyorApi creditConveyorApi;

    @Autowired
    private CalculateByIdService calculateByIdService;

    @Test
    void calculateById() {


        Application application = Application.builder()
                .applicationId(1L)
                .credit(Credit.builder()
                        .amount(BigDecimal.valueOf(1000000))
                        .term(36)
                        .build())
                .client(Client.builder()
                        .passport(Passport.builder()
                                .passportDTO(PassportDTO.builder()
                                        .series("1234")
                                        .number("567890")
                                        .build())
                                .build())
                        .build())
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(new ArrayList<>())
                .build();


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



        CreditDTO creditDTO = CreditDTO.builder()
                .amount(BigDecimal.valueOf(1000000))
                .term(36)
                .monthlyPayment(BigDecimal.valueOf(10000))
                .rate(BigDecimal.valueOf(20))
                .psk(BigDecimal.valueOf(1500000))
                .isInsuranceEnabled(true)
                .isSalaryClient(false)
                .build();

        when(applicationRepository.findById(1L)).thenReturn(java.util.Optional.of(application));
        when(creditConveyorApi.calculate(any(ScoringDataDTO.class))).thenReturn(creditDTO);

        calculateByIdService.calculateById(requestDTO, 1L);

        verify(applicationRepository, times(1)).findById(1L);
        verify(creditConveyorApi, times(1)).calculate(any(ScoringDataDTO.class));

    }
}