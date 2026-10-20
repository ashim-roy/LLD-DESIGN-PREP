package com.ashim.splitwise.service;

import com.ashim.splitwise.model.*;
import com.ashim.splitwise.strategy.SplitStrategy;

import java.util.ArrayList;
import java.util.List;

public class ExpenseService {

    /*
    Receives the expense details.
    Uses the injected SplitStrategy to calculate each participant's share.
    Creates an Expense, whose constructor validates the split totals and group membership.
    Stores and returns the expense.
     */

    private final SplitStrategy splitStrategy;
    private final List<Expense> expenses = new ArrayList<>();

    public ExpenseService(SplitStrategy splitStrategy) {
        if (splitStrategy == null) {
            throw new IllegalArgumentException(
                    "Split strategy cannot be null"
            );
        }
        this.splitStrategy = splitStrategy;
    }

    public Expense createExpense(
            String description,
            Group group,
            User paidBy,
            Money totalAmount,
            List<User> participants) {

        if (group == null) {
            throw new IllegalArgumentException("Group cannot be null");
        }

        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException(
                    "Participants cannot be empty"
            );
        }

        List<ExpenseSplit> splits = splitStrategy.split(totalAmount, participants);

        Expense expense = new Expense(description, group, paidBy, totalAmount, splits);

        expenses.add(expense);
        return expense;
    }

    public List<Expense> getExpenses() {
        return List.copyOf(expenses);
    }
}

// this code defines an `ExpenseService` class that manages expenses in a group setting. It uses a `SplitStrategy` to determine
// how to split the total amount of an expense among participants. The class provides methods to create new expenses and retrieve the list of all expenses.
// It also includes validation to ensure that the group and participants are not null or empty when creating an expense.
