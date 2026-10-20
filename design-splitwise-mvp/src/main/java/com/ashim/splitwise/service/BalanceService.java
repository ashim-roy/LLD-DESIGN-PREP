package com.ashim.splitwise.service;

import com.ashim.splitwise.model.*;

import java.math.BigDecimal;
import java.util.*;

public class BalanceService {


    /*
    Replace the existing implementation with the following. It returns signed BigDecimal balances:
        - Positive balance: the user should receive money.
        - Negative balance: the user owes money.
        - Zero balance: the user is settled.
    Important behavior: group-wise calculations include only settlements explicitly associated with that group.
    Global calculations include the settlements passed to them, including global settlements.
    Pass the expenses and settlements for the same intended scope.
     */
    public Map<User, BigDecimal> calculateGroupBalances(
            Group group,
            List<Expense> expenses,
            List<Settlement> settlements) {

        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        List<Expense> groupExpenses = expenses.stream()
                .filter(e -> e.getGroup().getGroupId()
                        .equals(group.getGroupId()))
                .toList();

        List<Settlement> groupSettlements = settlements.stream()
                .filter(s -> s.getGroup() != null &&
                        s.getGroup().getGroupId().equals(group.getGroupId()))
                .toList();

        return calculate(groupExpenses, groupSettlements);
    }

    public Map<User, BigDecimal> calculateGlobalBalances(
            List<Expense> expenses,
            List<Settlement> settlements) {

        return calculate(expenses, settlements);
    }

    private Map<User, BigDecimal> calculate(
            List<Expense> expenses,
            List<Settlement> settlements) {

        if (expenses == null || settlements == null) {
            throw new IllegalArgumentException(
                    "Expenses and settlements cannot be null"
            );
        }

        Map<User, BigDecimal> balances = new HashMap<>();
        String currency = null;

        for (Expense expense : expenses) {
            if (expense == null) {
                throw new IllegalArgumentException(
                        "Expenses cannot contain null entries"
                );
            }

            String expenseCurrency =
                    expense.getTotalAmount().getCurrency();

            currency = validateCurrency(currency, expenseCurrency);

            add(balances, expense.getPaidBy(),
                    expense.getTotalAmount().getAmount());

            for (ExpenseSplit split : expense.getSplits()) {
                add(balances, split.getUser(),
                        split.getAmount().getAmount().negate());
            }
        }

        for (Settlement settlement : settlements) {
            if (settlement == null) {
                throw new IllegalArgumentException(
                        "Settlements cannot contain null entries"
                );
            }

            String settlementCurrency =
                    settlement.getAmount().getCurrency();

            currency = validateCurrency(currency, settlementCurrency);

            // Debtor pays creditor: payer moves toward zero,
            // receiver moves toward zero.
            add(balances, settlement.getPayer(),
                    settlement.getAmount().getAmount());

            add(balances, settlement.getReceiver(),
                    settlement.getAmount().getAmount().negate());
        }

        return Map.copyOf(balances);
    }

    private String validateCurrency(
            String currentCurrency, String nextCurrency) {

        if (currentCurrency != null &&
                !currentCurrency.equals(nextCurrency)) {
            throw new IllegalArgumentException(
                    "Cannot calculate balances across different currencies"
            );
        }
        return nextCurrency;
    }

    private void add(
            Map<User, BigDecimal> balances,
            User user,
            BigDecimal amount) {

        balances.merge(user, amount, BigDecimal::add);
    }
}

