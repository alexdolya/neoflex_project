package ru.dolya.neoflex_project.service;

import java.math.BigDecimal;

public interface CreditRules {

    BigDecimal getBaseRate();
    BigDecimal getInsuranceAmount();
    BigDecimal getInsuranceDiscount();
    BigDecimal getSalaryClientDiscount();
}
