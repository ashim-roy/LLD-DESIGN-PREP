package com.ashim.pricedrop.notification;

import com.ashim.pricedrop.model.Money;
import com.ashim.pricedrop.model.Product;
import com.ashim.pricedrop.model.User;

public class ConsoleNotificationService implements NotificationService {
    @Override
    public void sendPriceDropNotification(
            User user,
            Product product,
            Money currentPrice,
            Money targetPrice) {

        System.out.println("🔔 Price Drop Alert!");
        System.out.println("User: " + user.getUsername());
        System.out.println("Product: " + product.getName());
        System.out.println("URL: " + product.getUrl());
        System.out.println("Current Price: " + currentPrice);
        System.out.println("Target Price: " + targetPrice);
    }
}

