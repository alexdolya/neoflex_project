package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.dolya.deal.client.CreditConveyorApi;
import ru.dolya.deal.exception.FeignClientCustomException;
import ru.dolya.deal.mapper.ClientMapper;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.Client;
import ru.dolya.deal.model.domain.StatusHistory;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;
import ru.dolya.deal.model.dto.LoanApplicationRequestDTO;
import ru.dolya.deal.model.dto.LoanOfferDTO;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.repository.ClientRepository;
import ru.dolya.deal.service.ApplicationService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ApplicationServiceImpl implements ApplicationService {

    private final ClientRepository clientRepository;
    private final ApplicationRepository applicationRepository;
    private final CreditConveyorApi creditConveyorApi;
    private final ClientMapper clientMapper;

    @Transactional
    public List<LoanOfferDTO> getOffers(LoanApplicationRequestDTO requestDTO) {

        Client client = clientMapper.getClientFromRequestDTO(requestDTO);

        log.info("Create client from LoanApplicationRequestDTO: {} {} Passport: {} {}",
                client.getLastName(),
                client.getFirstName(),
                client.getPassport().getPassportDTO().getSeries(),
                client.getPassport().getPassportDTO().getNumber());

        clientRepository.save(client);

        log.info("Successful save client to database");

        Application application = Application.builder()
                .client(client)
                .sesCode(UUID.randomUUID())
                .creationDate(LocalDate.now())
                .status(ApplicationStatus.PREAPPROVAL)
                .statusHistory(List.of(StatusHistory.builder()
                        .changeType(ChangeType.AUTOMATIC)
                        .status(ApplicationStatus.PREAPPROVAL)
                        .time(LocalDateTime.now())
                        .build()))
                .build();

        log.info("Create application and set status to PREAPPROVAL");

        Long appID = applicationRepository.save(application).getApplicationId();

        log.info("Save to database with ID: {}", appID);


        List<LoanOfferDTO> loanOfferDTOList;

        try {
            loanOfferDTOList = creditConveyorApi.getOffers(requestDTO).stream()
                    .peek(offer -> offer.setApplicationId(appID))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            throw new FeignClientCustomException(new Throwable(ex.getMessage()));
        }

        return loanOfferDTOList;
    }

}
