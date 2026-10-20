package com.ashim.splitwise.model;

import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {

    private final Map<User, Money> balances = new HashMap<>();

    public void addBalance(User user, Money amount){
        if(user == null || amount == null){
            throw new IllegalArgumentException("User and amount cannot be null");
        }

        Money currentBalance = balances.get(user);

        if(currentBalance == null){
            balances.put(user, amount);
        } else {
            balances.put(user, currentBalance.add(amount));
        }
    }

    public Map<User, Money> getBalances() {
        return Map.copyOf(balances);
    }

}
