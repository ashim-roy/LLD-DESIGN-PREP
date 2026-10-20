package com.ashim.splitwise.strategy;


import com.ashim.splitwise.model.ExpenseSplit;
import com.ashim.splitwise.model.Money;
import com.ashim.splitwise.model.User;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class EqualSplitStrategyTest {

    private final SplitStrategy strategy = new EqualSplitStrategy();

    @Test
    void shouldSplitAmountEquallyAmongParticipants() {
        User Alice = new User("Alice", "alice@example.com");
        User Bob = new User("Bob", "bob@example.com");
        User Charlie = new User("Charlie", "charlie@example.com");

        Money totalAmount = new Money(new BigDecimal("300.00"), "INR");
        List<ExpenseSplit> expenseSplits = strategy.split(totalAmount, List.of(Alice, Bob, Charlie));
        assertEquals(3, expenseSplits.size());

        for(ExpenseSplit expenseSplit : expenseSplits) {
            assertEquals(new Money(new BigDecimal("100.00"), "INR"), expenseSplit.getAmount());
        }

        assertEquals(Alice, expenseSplits.get(0).getUser());
        assertEquals(Bob, expenseSplits.get(1).getUser());
        assertEquals(Charlie, expenseSplits.get(2).getUser());

    }

    @Test
    void shouldRejectDuplicateParticipants() {
        User alice = new User("Alice", "alice@example.com");

        Money total = new Money(new BigDecimal("100.00"), "INR");

        assertThrows(
                IllegalArgumentException.class,
                () -> strategy.split(total, List.of(alice, alice))
        );
    }
}
