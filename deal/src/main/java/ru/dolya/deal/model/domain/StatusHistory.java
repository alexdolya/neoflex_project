package ru.dolya.deal.model.domain;

import lombok.Builder;
import lombok.Data;
import ru.dolya.deal.model.domain.enums.ApplicationStatus;
import ru.dolya.deal.model.domain.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
