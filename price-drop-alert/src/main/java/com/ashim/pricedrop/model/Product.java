package com.ashim.pricedrop.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Product {
    private final UUID id;
    private final String name;
    private final String url;
   // private final Money currentPrice;  // Your currentPrice field is declared final, but updatePrice() assigns a new value to it. Remove
    private Money currentPrice;  // Remove final modifier to allow updating the price

    public Product(String name, String url, Money currentPrice) {
        this.id = UUID.randomUUID();

        if(name == null) throw new NullPointerException("Product name cannot be null");
        if(url == null) throw new NullPointerException("Product URL cannot be null");
        if(currentPrice == null) throw new NullPointerException("Current price cannot be null");

        if(name.isBlank()) throw new IllegalArgumentException("Product name cannot be blank");
        if(url.isBlank()) throw new IllegalArgumentException("Product URL cannot be blank");

//        if (currentPrice.isLessThanOrEqualTo(new Money(BigDecimal.ZERO, currentPrice.getCurrency()))) {
//            throw new IllegalArgumentException("Current price must be positive");
//        }
        if (currentPrice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Current price must be positive");
        }

        this.name = name;
        this.url = url;
        this.currentPrice = currentPrice;

    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getUrl() {
        return url;
    }
    public Money getCurrentPrice() {
        return currentPrice;
    }

    public void updatePrice(Money newPrice) {
        if(newPrice == null) throw new NullPointerException("New price cannot be null");

        if (newPrice.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("New price must be positive");
        }

        this.currentPrice = newPrice;
    }

}
