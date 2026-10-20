package com.ashim.splitwise.strategy;

import com.ashim.splitwise.model.ExpenseSplit;
import com.ashim.splitwise.model.Money;
import com.ashim.splitwise.model.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public List<ExpenseSplit> split(
            Money totalAmount,
            List<User> participants) {

        if (totalAmount == null) {
            throw new IllegalArgumentException("Total amount cannot be null");
        }

        if (participants == null || participants.isEmpty()) {
            throw new IllegalArgumentException("Participants cannot be null or empty");
        }

        // Check for null users in participants
        if (participants.stream().anyMatch(user -> user == null)) {
            throw new IllegalArgumentException("Participants cannot contain null users");
        }

        // Check for duplicate users in participants
//        Set<User> uniqueParticipants = new HashSet<>(participants);
//
//        if (uniqueParticipants.size() != participants.size()) {
//            throw new IllegalArgumentException("Duplicate participants are not allowed");
//        }
        if (new HashSet<>(participants).size() != participants.size()) {
            throw new IllegalArgumentException("Duplicate participants are not allowed");
        }

        Money splitAmount = totalAmount.divide(participants.size());

        return participants.stream()
                .map(user -> new ExpenseSplit(user, splitAmount))
                .toList();
    }
}

