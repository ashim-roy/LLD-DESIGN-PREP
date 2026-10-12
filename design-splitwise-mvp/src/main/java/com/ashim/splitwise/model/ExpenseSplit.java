package com.ashim.splitwise.model;

public class ExpenseSplit {
    private final User user;
    private final Money amount;

    public ExpenseSplit(User user, Money amount) {
        if (user == null) {
            throw new IllegalArgumentException("ExpenseSplit user cannot be null");
        }
        if (amount == null) {
            throw new IllegalArgumentException("ExpenseSplit amount cannot be null");
        }
        this.user = user;
        this.amount = amount;
    }

    public User getUser() {
        return user;
    }

    public Money getAmount() {
        return amount;
    }
}
