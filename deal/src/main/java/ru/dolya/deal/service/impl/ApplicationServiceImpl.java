package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.client.CreditConveyorOffersApi;
import ru.dolya.deal.domain.*;
import ru.dolya.deal.domain.enums.ApplicationStatus;
import ru.dolya.deal.domain.enums.ChangeType;
import ru.dolya.deal.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.dto.LoanOfferDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.repository.ClientRepository;
import ru.dolya.deal.service.ApplicationService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditConveyorOffersApi creditConveyorOffersApi;

    @Transactional
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {

        Client client = Client.builder()
                .firstName(requestDTO.getFirstName())
                .middleName(requestDTO.getMiddleName())
                .lastName(requestDTO.getLastName())
                .birthDate(requestDTO.getBirthdate())
                .email(requestDTO.getEmail())
                .passport(Passport.builder()
                        .number(requestDTO.getPassportNumber())
                        .series(requestDTO.getPassportSeries())
                        .build())
                .build();

        clientRepository.save(client);

        Application application = Application.builder()
                .client(client)
                .creationDate(LocalDate.now())
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(List.of(StatusHistory.builder()
                        .changeType(ChangeType.AUTOMATIC)
                        .status(ApplicationStatus.PREAPPROVAL)
                        .time(LocalDateTime.now())
                        .build()))
                .build();

        Long appID = applicationRepository.save(application).getApplicationId();


        return creditConveyorOffersApi.getOffers(requestDTO).stream()
                .peek(offer -> offer.setApplicationId(appID))
                .collect(Collectors.toList());
    }

}
