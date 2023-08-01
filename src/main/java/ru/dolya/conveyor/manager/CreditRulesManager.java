package ru.dolya.conveyor.manager;

import java.math.BigDecimal;

public interface CreditRulesManager {

    BigDecimal getBaseRate();
    BigDecimal getInsuranceAmount();
    BigDecimal getInsuranceDiscount();
    BigDecimal getSalaryClientDiscount();
}
