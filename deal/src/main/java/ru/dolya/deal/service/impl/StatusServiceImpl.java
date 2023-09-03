package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.StatusHistory;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.StatusService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class StatusServiceImpl implements StatusService {

    private final ApplicationRepository applicationRepository;
    @Override
    public void setStatus(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        List<StatusHistory> statusHistoryList = application.getStatusHistory();
        statusHistoryList.add(StatusHistory.builder()
                .changeType(ChangeType.AUTOMATIC)
                .status(ApplicationStatus.DOCUMENT_CREATED)
                .time(LocalDateTime.now())
                .build());
        application.setStatusHistory(statusHistoryList);
        applicationRepository.save(application);
        log.info("Application status set to DOCUMENT_CREATED");
    }
}
