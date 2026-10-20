package com.ashim.splitwise.strategy;

import com.ashim.splitwise.model.ExpenseSplit;
import com.ashim.splitwise.model.Money;
import com.ashim.splitwise.model.User;

import java.util.List;

public interface SplitStrategy {
    /**
     * Splits the total amount among the given users according to the strategy.
     *
     * @param totalAmount The total amount to be split.
     * @param participants       The users among whom the amount is to be split.
     * @return An array of ExpenseSplit objects representing the split amounts for each user.
     */

        List<ExpenseSplit> split(
                Money totalAmount,
                List<User> participants
        );
}
