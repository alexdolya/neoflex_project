package ru.dolya.conveyor.manager.impl;

import org.springframework.stereotype.Component;
import ru.dolya.conveyor.config.ConfigCreditRules;
import ru.dolya.conveyor.manager.CreditRulesManager;

import java.math.BigDecimal;

@Component
public class CreditRulesManagerImpl implements CreditRulesManager {


    ConfigCreditRules configCreditRules;

    CreditRulesManagerImpl(ConfigCreditRules configCreditRules) {
        this.configCreditRules = configCreditRules;
    }

    @Override
    public BigDecimal getBaseRate() {
        return configCreditRules.getBaseRate();
    }

    @Override
    public BigDecimal getInsuranceAmount() {
        return configCreditRules.getInsuranceAmount();
    }

    @Override
    public BigDecimal getInsuranceDiscount() {
        return configCreditRules.getInsuranceDiscount();
    }

    @Override
    public BigDecimal getSalaryClientDiscount() {
        return configCreditRules.getSalaryClientDiscount();
    }


}
