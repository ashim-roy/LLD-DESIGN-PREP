package com.ashim.splitwise.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Locale;
import java.util.Objects;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if(amount == null || currency == null){
            throw new IllegalArgumentException("Amount and currency cannot be null");
        }

        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

       // if(currency.equals(" ")) {
        if(currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be empty");
        }

        // Your currency.trim().toUpperCase(Locale.ROOT) is good. Consider validating the normalized currency as a three-letter code, such as INR or USD.
        this.currency = currency.trim().toUpperCase(Locale.ROOT);
        this.amount = amount;
       // this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    // Add Money addition method

    public Money add(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Money cannot be null");
        }

        if (!this.currency.equals(other.getCurrency())) {
            throw new IllegalArgumentException(
                    "Cannot add Money with different currencies");
        }

        BigDecimal newAmount = this.amount.add(other.getAmount());

        return new Money(newAmount, this.currency);
    }

    public Money divide(int divisor) {
        if (divisor <= 0) {
            throw new IllegalArgumentException(
                    "Divisor must be greater than zero"
            );
        }

        return new Money(amount.divide(BigDecimal.valueOf(divisor), 2, RoundingMode.DOWN),
                currency
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof Money other)) {
            return false;
        }

        return amount.compareTo(other.amount) == 0
                && currency.equals(other.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                amount.stripTrailingZeros(),
                currency
        );
    }

    @Override
    public String toString() {
        return currency + " " + amount.toPlainString();
    }



}
