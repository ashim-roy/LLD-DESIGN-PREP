//package com.ashim.splitwise.service;
//
//import com.ashim.splitwise.model.Expense;
//import com.ashim.splitwise.model.ExpenseSplit;
//import com.ashim.splitwise.model.User;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//// Calculate balances for one group or across multiple groups, accounting for recorded settlements.
//public class BalanceService {
//    public Map<User, BigDecimal> calculateBalance(List<Expense> expenses) {
//
//        if(expenses == null){
//            throw new IllegalArgumentException("Expenses list cannot be null");
//        }
//
//        Map<User, BigDecimal> balanceMap = new HashMap<>();
//        for(Expense expense : expenses){
//            if(expense == null){
//                throw new IllegalArgumentException("Expenses cannot contain null entries");
//            }
//
//            User payer = expense.getPaidBy();
//
//            // Credit the payer with the full expense amount.
//            addAmount(
//                    balanceMap,
//                    payer,
//                    expense.getTotalAmount().getAmount()
//            );
//
//            // Debit each participant by their share.
//            for (ExpenseSplit split : expense.getSplits()) {
//                addAmount(
//                        balanceMap,
//                        split.getUser(),
//                        split.getAmount().getAmount().negate()
//                );
//            }
//        }
//
//        return Map.copyOf(balanceMap);
//    }
//
//    private void addAmount(
//            Map<User, BigDecimal> balances,
//            User user,
//            BigDecimal amount) {
//
//        balances.merge(user, amount, BigDecimal::add);
//    }
//}
//
///*
//The service will process each expense:
//1. Add the full expense amount to the payer's balance.
//2. Subtract each participant's split amount from their balance.
//3. Aggregate the results across all expenses.
// */