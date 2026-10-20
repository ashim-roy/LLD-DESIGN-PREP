package com.ashim.splitwise.service;


import com.ashim.splitwise.model.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class DebtSimplifier {

    /*
    This uses a greedy matching approach: take one debtor and one creditor, settle the smaller outstanding amount,
    then advance whichever side is settled. It reduces the number of payments in many cases, though it doesn't guarantee the mathematically minimum number for every possible balance distribution.

    For group-wise simplification, call it with the balances returned by calculateGroupBalances.
    For global simplification, call it with the balances returned by calculateGlobalBalances.
     */
    public List<PaymentSuggestion> simplify(
            Map<User, BigDecimal> balances,
            String currency) {

        if (balances == null || currency == null || currency.isBlank()) {
            throw new IllegalArgumentException("Balances and currency are required");
        }

        List<Map.Entry<User, BigDecimal>> debtors = new ArrayList<>();
        List<Map.Entry<User, BigDecimal>> creditors = new ArrayList<>();

        BigDecimal total = balances.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (total.compareTo(new BigDecimal("0.01")) > 0 ||
                total.compareTo(new BigDecimal("-0.01")) < 0) {
            throw new IllegalArgumentException(
                    "Net balances must sum to zero"
            );
        }

        balances.forEach((user, balance) -> {
            if (user == null || balance == null) {
                throw new IllegalArgumentException(
                        "Balances cannot contain null entries"
                );
            }

            if (balance.signum() < 0) {
                debtors.add(Map.entry(user, balance.negate()));
            } else if (balance.signum() > 0) {
                creditors.add(Map.entry(user, balance));
            }
        });

        List<BigDecimal> debtAmounts = debtors.stream()
                .map(Map.Entry::getValue)
                .collect(java.util.stream.Collectors.toCollection(
                        ArrayList::new));

        List<BigDecimal> creditAmounts = creditors.stream()
                .map(Map.Entry::getValue)
                .collect(java.util.stream.Collectors.toCollection(
                        ArrayList::new));

        List<PaymentSuggestion> suggestions = new ArrayList<>();
        int d = 0;
        int c = 0;

        while (d < debtors.size() && c < creditors.size()) {
            BigDecimal payment = debtAmounts.get(d)
                    .min(creditAmounts.get(c))
                    .setScale(2, RoundingMode.DOWN);

            if (payment.signum() > 0) {
                suggestions.add(new PaymentSuggestion(
                        debtors.get(d).getKey(),
                        creditors.get(c).getKey(),
                        new Money(payment, currency)
                ));
            }

            debtAmounts.set(d, debtAmounts.get(d).subtract(payment));
            creditAmounts.set(c, creditAmounts.get(c).subtract(payment));

            if (debtAmounts.get(d).signum() == 0) d++;
            if (creditAmounts.get(c).signum() == 0) c++;
        }

        return List.copyOf(suggestions);
    }
}

