package ru.dolya.deal.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class EmailMessage {
    private String address;
    private Theme theme;
    private Long applicationId;
}
