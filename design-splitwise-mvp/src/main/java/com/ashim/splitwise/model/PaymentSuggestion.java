package com.ashim.splitwise.model;

public record PaymentSuggestion(
        User from,
        User to,
        Money amount) {

    public PaymentSuggestion {
        if (from == null || to == null || amount == null) {
            throw new IllegalArgumentException(
                    "Suggestion fields cannot be null"
            );
        }
        if (from.equals(to)) {
            throw new IllegalArgumentException(
                    "From and to users must be different"
            );
        }
        if (amount.getAmount().signum() <= 0) {
            throw new IllegalArgumentException(
                    "Suggestion amount must be positive"
            );
        }
    }
}
