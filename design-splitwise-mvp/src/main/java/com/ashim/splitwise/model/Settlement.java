package com.ashim.splitwise.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

public class Settlement {

    private final UUID settlementId;
    private final User payer;
    private final User receiver;
    private final Money amount;
    private final Group group; // null means global settlement
    private final LocalDateTime createdAt;

    public Settlement(
            User payer,
            User receiver,
            Money amount,
            Group group) {

        if (payer == null || receiver == null) {
            throw new IllegalArgumentException(
                    "Payer and receiver cannot be null"
            );
        }
        if (payer.equals(receiver)) {
            throw new IllegalArgumentException(
                    "Payer and receiver must be different users"
            );
        }
        if (amount == null ||
                amount.getAmount().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "Settlement amount must be greater than zero"
            );
        }

        if (group != null) {
            if (!group.getMembers().contains(payer) || !group.getMembers().contains(receiver)) {
                throw new IllegalArgumentException(
                        "Both users must belong to the settlement group"
                );
            }
        }

        this.settlementId = UUID.randomUUID();
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.group = group;
        this.createdAt = LocalDateTime.now();
        // this.createdAt = Instant.now();
    }

    public UUID getSettlementId() { return settlementId; }
    public User getPayer() { return payer; }
    public User getReceiver() { return receiver; }
    public Money getAmount() { return amount; }
    public Group getGroup() { return group; }
    public boolean isGlobal() { return group == null; }
    //public Instant getCreatedAt() { return createdAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}

