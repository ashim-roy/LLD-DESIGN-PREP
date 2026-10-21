package com.ashim.pricedrop.service;

import com.ashim.pricedrop.model.*;
import com.ashim.pricedrop.notification.NotificationService;
import com.ashim.pricedrop.repository.PriceAlertRepository;
import com.ashim.pricedrop.repository.ProductRepository;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class PriceAlertService {

    // This will be the core of our application: creating alerts, checking target prices, preventing duplicate notifications, and deactivating alerts.
    private final ProductRepository productRepository;
    private final PriceAlertRepository priceAlertRepository;
    private final NotificationService notificationService;

    public PriceAlertService( ProductRepository productRepository, PriceAlertRepository priceAlertRepository, NotificationService notificationService){
//        this.productRepository = productRepository;
//        this.priceAlertRepository = priceAlertRepository;
//        this.notificationService = notificationService;
        // Validate dependencies using Objects.requireNonNull() so invalid dependencies fail immediately.
        this.productRepository = Objects.requireNonNull(productRepository);
        this.priceAlertRepository = Objects.requireNonNull(priceAlertRepository);
        this.notificationService = Objects.requireNonNull(notificationService);
    }

    public PriceAlert createAlert(User user, UUID productId, Money targetPrice){
        /*
        createAlert(user, productId, targetPrice)
            - Find the product using ProductRepository.
            - Validate the target price and its currency against the product price.
            - Create a PriceAlert.
            - Save and return the alert.
         */
       // Product product = productRepository.findById(productId).orElseThrow();
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found: " + productId));

        Money currentPrice = product.getCurrentPrice();

        if(!currentPrice.getCurrency().equals(targetPrice.getCurrency()) ){
            throw new IllegalArgumentException("target price currency mismatch");
        }

        if(currentPrice.isLessThanOrEqualTo(targetPrice)){
            throw new IllegalArgumentException("target price should be less than current price");
        }

        PriceAlert alert = new PriceAlert(user, product, targetPrice);
        return priceAlertRepository.save(alert);
    }


    /*
    2. checkPriceDrop(productId)
        - Retrieve the product.
        - Find all alerts associated with it.
        - Consider only active alerts that haven't been notified.
        - Check whether currentPrice <= targetPrice.
        - Notify the user and mark the alert as notified.
     */
    public void checkPriceDrop(UUID productId){
//        Product product = productRepository.findById(productId).orElseThrow();
//        List<PriceAlert> allAlerts = priceAlertRepository.findByProductId( productId);
//        List<PriceAlert> activeAlerts = allAlerts.stream().filter(alert -> alert.getStatus().equals(AlertStatus.ACTIVE)).toList();
        List<PriceAlert> eligibleAlerts = priceAlertRepository
                .findByProductId(productId)
                .stream()
                .filter(alert -> alert.getStatus() == AlertStatus.ACTIVE)
                .filter(alert -> !alert.isNotified())
                .toList();

        // Check whether currentPrice <= targetPrice.
        // Product product = productRepository.findById(productId).orElseThrow();
        Product product = productRepository.findById(productId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Product not found: " + productId));

        Money currentPrice = product.getCurrentPrice();

        /*
        Next, iterate through eligibleAlerts. For each alert:
            1. Get its target price.
            2. Check whether currentPrice.isLessThanOrEqualTo(targetPrice).
            3. If the condition matches, call notificationService to notify the user.
            4. Only after notification succeeds, call alert.markAsNotified().
         */
        for(PriceAlert eligibleAlert: eligibleAlerts){
            Money targetPrice = eligibleAlert.getTargetPrice();
            if(currentPrice.isLessThanOrEqualTo(targetPrice)){
                notificationService.sendPriceDropNotification(
                        eligibleAlert.getUser(),
                        product,
                        currentPrice,
                        targetPrice);
                // Mark notified only after successful notification
                eligibleAlert.markAsNotified();
            }
        }

    }

}
