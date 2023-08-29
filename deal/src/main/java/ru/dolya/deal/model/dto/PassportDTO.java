package ru.dolya.deal.model.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class PassportDTO {

    private String series;
    private String number;
    private String issueBranch;
    private LocalDate issueDate;

}