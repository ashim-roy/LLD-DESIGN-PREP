package com.ashim.pricedrop.model;

import java.math.BigDecimal;
import java.util.UUID;

public class PriceAlert {
    private final UUID id;
    private final User user;
    private final Product product;
    private final Money targetPrice;
    private AlertStatus status;
    private boolean notified;

    public PriceAlert(User user, Product product, Money targetPrice) {

        if(user == null) throw new NullPointerException("User cannot be null");
        if(product == null) throw new NullPointerException("Product cannot be null");
        if(targetPrice == null) throw new NullPointerException("Target price cannot be null");
        if(targetPrice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Target price must be positive");
        }


        this.id = UUID.randomUUID();
        this.user = user;
        this.product = product;
        this.targetPrice = targetPrice;
        this.status = AlertStatus.ACTIVE;
        this.notified = false;
    }

    public UUID getId() {
        return id;
    }
    public User getUser() {
        return user;
    }
    public Product getProduct() {
        return product;
    }
    public Money getTargetPrice() {
        return targetPrice;
    }
    public AlertStatus getStatus() {
        return status;
    }

    public boolean isNotified() {
        return notified;
    }

    public void deactivate() {
        this.status = AlertStatus.INACTIVE;
    }

    public void markAsNotified() {
        this.notified = true;
    }

}
