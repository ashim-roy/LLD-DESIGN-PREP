package com.ashim.pricedrop.notification;

import com.ashim.pricedrop.model.Money;
import com.ashim.pricedrop.model.Product;
import com.ashim.pricedrop.model.User;

public interface NotificationService {
    void sendPriceDropNotification(
            User user,
            Product product,
            Money currentPrice,
            Money targetPrice
    );
}
