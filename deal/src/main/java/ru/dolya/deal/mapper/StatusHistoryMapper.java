package ru.dolya.deal.mapper;

import org.springframework.stereotype.Component;
import ru.dolya.deal.model.domain.StatusHistory;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class StatusHistoryMapper {
    public List<StatusHistory> updateStatusHistory(List<StatusHistory> statusHistoryList, ApplicationStatus applicationStatus) {
        statusHistoryList.add(StatusHistory.builder()
                .changeType(ChangeType.AUTOMATIC)
                .status(applicationStatus)
                .time(LocalDateTime.now())
                .build());
        return statusHistoryList;
    }

}
