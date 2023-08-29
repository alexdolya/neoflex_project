package ru.dolya.conveyor.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;

@Configuration
@Data
public class ConfigCreditRules {
    @Value("${creditRules.baseRate}")
    private BigDecimal baseRate;
    @Value("${creditRules.insuranceAmount}")
    private BigDecimal insuranceAmount;
    @Value("${creditRules.insuranceDiscount}")
    private BigDecimal insuranceDiscount;
    @Value("${creditRules.salaryClientDiscount}")
    private BigDecimal salaryClientDiscount;
}
