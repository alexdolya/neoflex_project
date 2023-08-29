package ru.dolya.conveyor.manager;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.dolya.conveyor.ConveyorApplication;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ConveyorApplication.class)

class CreditRulesManagerTest {
    @Autowired
    CreditRulesManager creditRulesManager;


    @Test
    void getBaseRate() {
        assertEquals(BigDecimal.valueOf(20), creditRulesManager.getBaseRate());
    }

    @Test
    void getInsuranceAmount() {
        assertEquals(BigDecimal.valueOf(20000), creditRulesManager.getInsuranceAmount());
    }

    @Test
    void getInsuranceDiscount() {
        assertEquals(BigDecimal.valueOf(3), creditRulesManager.getInsuranceDiscount());
    }

    @Test
    void getSalaryClientDiscount() {
        assertEquals(BigDecimal.valueOf(1), creditRulesManager.getSalaryClientDiscount());
    }
}