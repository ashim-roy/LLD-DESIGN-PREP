package com.ashim.splitwise.service;

import com.ashim.splitwise.model.*;

import java.util.ArrayList;
import java.util.List;

public class SettlementService {

    private final List<Settlement> settlements = new ArrayList<>();

    public Settlement recordSettlement(
            User payer,
            User receiver,
            Money amount,
            Group group) {

        Settlement settlement =
                new Settlement(payer, receiver, amount, group);

        settlements.add(settlement);
        return settlement;
    }

    public List<Settlement> getSettlements() {
        return List.copyOf(settlements);
    }
}

// This deliberately keeps payment recording separate from debt simplification. It doesn't yet verify that the payer owes the receiver that amount; that requires checking current outstanding balances before accepting a payment. We'll want that validation before treating this as production-ready.
