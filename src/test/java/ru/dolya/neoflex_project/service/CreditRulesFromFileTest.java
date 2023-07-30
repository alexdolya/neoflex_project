package ru.dolya.neoflex_project.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CreditRulesFromFileTest {

    @InjectMocks
    CreditRulesFromFile creditRulesFromFile;



    @Test
    void getBaseRate() {
        assertEquals(BigDecimal.valueOf(20.0), creditRulesFromFile.getBaseRate());
    }

    @Test
    void getInsuranceAmount() {
        assertEquals(BigDecimal.valueOf(20000.0), creditRulesFromFile.getInsuranceAmount());
    }

    @Test
    void getInsuranceDiscount() {
        assertEquals(BigDecimal.valueOf(3.0), creditRulesFromFile.getInsuranceDiscount());
    }

    @Test
    void getSalaryClientDiscount() {
        assertEquals(BigDecimal.valueOf(1.0), creditRulesFromFile.getSalaryClientDiscount());
    }
}