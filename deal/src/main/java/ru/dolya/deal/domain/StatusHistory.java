package ru.dolya.deal.domain;

import lombok.Builder;
import lombok.Data;
import ru.dolya.deal.domain.enums.ApplicationStatus;
import ru.dolya.deal.domain.enums.ChangeType;

import java.time.LocalDateTime;

@Data
@Builder
public class StatusHistory {
    private ApplicationStatus status;
    private LocalDateTime time;
    private ChangeType changeType;
}
