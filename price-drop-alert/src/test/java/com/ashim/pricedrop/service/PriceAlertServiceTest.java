package com.ashim.pricedrop.service;

import com.ashim.pricedrop.model.*;
import com.ashim.pricedrop.notification.NotificationService;
import com.ashim.pricedrop.repository.PriceAlertRepository;
import com.ashim.pricedrop.repository.ProductRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import com.ashim.pricedrop.model.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PriceAlertServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private PriceAlertRepository priceAlertRepository;

    @Mock
    private NotificationService notificationService;

    private PriceAlertService priceAlertService;

    private User user;
    private Product product;

    private final UUID productId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        priceAlertService = new PriceAlertService(
                productRepository,
                priceAlertRepository,
                notificationService
        );

        user = new User("ashim", "ashim@example.com");

        product = new Product(
                "Headphones",
                "https://example.com/headphones",
                money("100.00", "INR")
        );

        when(productRepository.findById(productId))
                .thenReturn(Optional.of(product));

//        when(priceAlertRepository.save(any(PriceAlert.class)))
//                .thenAnswer(invocation -> invocation.getArgument(0));
    }

    // Scenario 1: Target price is equal to or above current price.
    @Test
    void createAlert_shouldRejectTargetPriceEqualToOrAboveCurrentPrice() {
        Money equalPrice = money("100.00", "INR");
        Money higherPrice = money("110.00", "INR");

        assertThrows(
                IllegalArgumentException.class,
                () -> priceAlertService.createAlert(user, productId, equalPrice)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> priceAlertService.createAlert(user, productId, higherPrice)
        );

        verify(priceAlertRepository, never()).save(any(PriceAlert.class));
    }

    // Scenario 2: Target price currency differs from product currency.
    @Test
    void createAlert_shouldRejectCurrencyMismatch() {
        Money targetPrice = money("80.00", "USD");

        assertThrows(
                IllegalArgumentException.class,
                () -> priceAlertService.createAlert(user, productId, targetPrice)
        );

        verify(priceAlertRepository, never()).save(any(PriceAlert.class));
    }

    // Scenario 3: Current price remains above target.
    @Test
    void checkPriceDrop_shouldNotNotifyWhenTargetIsNotReached() {
        PriceAlert alert = createAlert("80.00");

        when(priceAlertRepository.findByProductId(productId))
                .thenReturn(List.of(alert));

        priceAlertService.checkPriceDrop(productId);

        verifyNoInteractions(notificationService);
        assertFalse(alert.isNotified());
        assertEquals(AlertStatus.ACTIVE, alert.getStatus());
    }

    // Scenario 4: Current price reaches target.
    @Test
    void checkPriceDrop_shouldNotifyAndMarkAlertAsNotified() {
        PriceAlert alert = createAlert("80.00");

        when(priceAlertRepository.findByProductId(productId))
                .thenReturn(List.of(alert));

        product.updatePrice(money("80.00", "INR"));

        priceAlertService.checkPriceDrop(productId);

        verify(notificationService).sendPriceDropNotification(
                user,
                product,
                money("80.00", "INR"),
                alert.getTargetPrice()
        );

        assertTrue(alert.isNotified());
    }

    // Scenario 5: Further price drops must not cause duplicate notifications.
    @Test
    void checkPriceDrop_shouldNotSendDuplicateNotification() {
        PriceAlert alert = createAlert("80.00");

        when(priceAlertRepository.findByProductId(productId))
                .thenReturn(List.of(alert));

        // First check: target is reached.
        product.updatePrice(money("80.00", "INR"));
        priceAlertService.checkPriceDrop(productId);

        assertTrue(alert.isNotified());

        // Second check: price drops further.
        product.updatePrice(money("70.00", "INR"));
        priceAlertService.checkPriceDrop(productId);

        verify(notificationService, times(1))
                .sendPriceDropNotification(
                        user,
                        product,
                        money("80.00", "INR"),
                        alert.getTargetPrice()
                );
    }

    private PriceAlert createAlert(String targetAmount) {

        when(priceAlertRepository.save(any(PriceAlert.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        return priceAlertService.createAlert(
                user,
                productId,
                money(targetAmount, "INR")
        );
    }

    private Money money(String amount, String currency) {
        return new Money(new BigDecimal(amount), currency);
    }
}
