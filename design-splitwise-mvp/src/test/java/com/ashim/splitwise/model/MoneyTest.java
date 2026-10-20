package com.ashim.splitwise.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTest {

    @Test
    void shouldTreatAmountWithDiffScalesAsEqual(){
        Money money1 = new Money(new BigDecimal("100.0"), "INR");
        Money money2 = new Money(new BigDecimal("100.00"), "INR");

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void shouldWorkCorrectlyInHashSet() {
        Set<Money> moneySet = new HashSet<>();

        moneySet.add(new Money(new BigDecimal("100.0"), "INR"));
        moneySet.add(new Money(new BigDecimal("100.00"), "INR"));

        assertEquals(1, moneySet.size()); // checks that only one unique Money object is present in the set
    }

    @Test
    void shouldAddMoneyWithSameCurrency(){

        Money money1 = new Money(new BigDecimal("100.0"), "INR");
        Money money2 = new Money(new BigDecimal("50.0"), "INR");

        Money result = money1.add(money2);

        assertEquals(new BigDecimal("150.0"), result.getAmount());
        assertEquals("INR", result.getCurrency());
    }

    @Test
    void shouldRejectNegativeAmount() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Money(new BigDecimal("-10.00"), "INR")
        );
    }


}
