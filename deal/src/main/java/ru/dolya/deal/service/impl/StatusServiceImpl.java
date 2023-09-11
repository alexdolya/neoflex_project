package ru.dolya.deal.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import ru.dolya.deal.mapper.StatusHistoryMapper;
import ru.dolya.deal.model.domain.Application;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.repository.ApplicationRepository;
import ru.dolya.deal.service.StatusService;


@Service
@RequiredArgsConstructor
@Log4j2
public class StatusServiceImpl implements StatusService {

    private final ApplicationRepository applicationRepository;
    private final StatusHistoryMapper statusHistoryMapper;

    @Override
    public void setStatus(Long applicationId) {
        Application application = applicationRepository.findById(applicationId).orElseThrow();
        application.setStatus(ApplicationStatus.DOCUMENT_CREATED);
        application.setStatusHistory(statusHistoryMapper
                .updateStatusHistory(application.getStatusHistory(), ApplicationStatus.DOCUMENT_CREATED));
        applicationRepository.save(application);
        log.info("Application status set to DOCUMENT_CREATED");
    }
}
