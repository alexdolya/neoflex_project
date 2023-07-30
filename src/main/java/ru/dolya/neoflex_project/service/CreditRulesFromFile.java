package ru.dolya.neoflex_project.service;

import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.util.Properties;

@Service
public class CreditRulesFromFile implements CreditRules {
    @Override
    public BigDecimal getBaseRate() {
        return getCreditParameter("baseRate");
    }

    @Override
    public BigDecimal getInsuranceAmount() {
        return getCreditParameter("insuranceAmount");
    }

    @Override
    public BigDecimal getInsuranceDiscount() {
        return getCreditParameter("insuranceDiscount");
    }

    @Override
    public BigDecimal getSalaryClientDiscount() {
        return getCreditParameter("salaryClientDiscount");
    }


    private BigDecimal getCreditParameter(String property) {
        Properties properties = new Properties();
        BigDecimal creditParameter = null;
        try (InputStream inputStream = new FileInputStream("./src/main/resources/creditRules.properties")) {
            properties.load(inputStream);
            String baseRateString = properties.getProperty(property);
            creditParameter = BigDecimal.valueOf(Double.parseDouble(baseRateString));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return creditParameter;
    }


}
