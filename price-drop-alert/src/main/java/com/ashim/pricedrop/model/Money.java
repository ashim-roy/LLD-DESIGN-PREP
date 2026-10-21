package com.ashim.pricedrop.model;

import java.math.BigDecimal;
import java.util.Locale;

public class Money {
    private final BigDecimal amount;
    private final String currency;

    public Money(BigDecimal amount, String currency) {
        if(amount == null || currency == null) {
            throw new IllegalArgumentException("Amount and currency cannot be null");
        }
        if(currency.isBlank()) {
            throw new IllegalArgumentException("Currency cannot be blank");
        }
       // if(amount <= 0 ) {
        if(amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative");
        }

        this.amount = amount;
        this.currency = currency.trim().toUpperCase(Locale.ROOT);
    }

    public BigDecimal getAmount() {
        return amount;
    }
    public String getCurrency(){
        return this.currency;
    }

    public boolean isLessThanOrEqualTo(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Money cannot be null");
        }
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must match");
        }
        return this.amount.compareTo(other.amount) <= 0; // compares two amount and returns true if this amount is less than or equal to the other amount
    }

    // Reject comparisons between different currencies
    public boolean compareCurrency(Money other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Money cannot be null");
        }
        if (!this.currency.equals(other.currency)) { // equals is a String method that compares two strings and returns true if they are equal
            throw new IllegalArgumentException("Currencies must match");
        }
        return true;
    }

    // Implement equals() and hashCode() consistently
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money other = (Money) obj;
        return this.amount.equals(other.amount) && this.currency.equals(other.currency);
    }

   @Override
    public int hashCode() {
        int result = amount.hashCode(); // generates hashcode for amount, i.e. if two Money objects have the same amount, they will have the same hashcode for amount
        result = 31 * result + currency.hashCode(); // generates hashcode for currency, i.e. for 10 inr the result will be 31 * 10 + inr.hashcode() and for 10 usd the result will be 31 * 10 + usd.hashcode() which will be different// here 31 is a prime number used to reduce hash collisions
        return result;
    }

    @Override
    public String toString() {
        return "Money{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                '}';
        // the output will be Money{amount=10, currency='USD'} for 10 USD
    }

}
