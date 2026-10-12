package com.ashim.splitwise.model;

import java.util.List;
import java.util.UUID;

public class Expense {
    private final UUID expenseId;
    private final String description;
    private final Group group;
    private final User paidBy;
    private final Money totalAmount;
    private final List<ExpenseSplit> splits;

    public Expense(String description, Group group, User paidBy, Money totalAmount, List<ExpenseSplit> splits) {

        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Expense description cannot be null or blank");
        }
        if (group == null) {
            throw new IllegalArgumentException("Expense group cannot be null");
        }
        if (paidBy == null) {
            throw new IllegalArgumentException("Expense paidBy cannot be null");
        }
//        if (!group.getMembers()
//                .map(members -> members.contains(paidBy))
//                .orElse(false)) {
//            throw new IllegalArgumentException("Expense paidBy must be a member of the expense group");
//        }
        if (!group.getMembers().contains(paidBy)) {
            throw new IllegalArgumentException("Expense paidBy must be a member of the expense group");
        }

        if (totalAmount == null) {
            throw new IllegalArgumentException("Expense totalAmount cannot be null");
        }
        if (splits == null || splits.isEmpty()) {
            throw new IllegalArgumentException("Expense splits cannot be null or empty");
        }
        //Every participant in a split must belong to the group.
//        for (ExpenseSplit split : splits) {
//            if (!group.getMembers()
//                    .map(members -> members.contains(split.getUser()))
//                    .orElse(false)) {
//                throw new IllegalArgumentException("Every participant in a split must belong to the group");
//            }
//        }
        // Every participant in a split must belong to the group.
        for (ExpenseSplit split : splits) {
            if (split == null) {
                throw new IllegalArgumentException(
                        "Expense split cannot be null"
                );
            }

            if (!group.getMembers().contains(split.getUser())) {
                throw new IllegalArgumentException(
                        "Every participant in a split must belong to the group"
                );
            }
        }

        //The sum of all split amounts must equal totalAmount.
        Money sumOfSplits = splits.stream()
                .map(ExpenseSplit::getAmount)
                .reduce(Money::add)
                .orElseThrow(() -> new IllegalArgumentException("Expense splits cannot be null or empty"));
        if (!sumOfSplits.equals(totalAmount)) {
            throw new IllegalArgumentException("The sum of all split amounts must equal totalAmount");
        }

        this.expenseId = UUID.randomUUID();
        this.description = description;
        this.group = group;
        this.paidBy = paidBy;
        this.totalAmount = totalAmount;
        this.splits = List.copyOf(splits);
    }

    public UUID getExpenseId() {
        return expenseId;
    }
    public String getDescription() {
        return description;
    }
    public Group getGroup() { return group; }
    public User getPaidBy() { return paidBy; }
    public Money getTotalAmount() { return totalAmount; }
    public List<ExpenseSplit> getSplits() { return splits; }

}
