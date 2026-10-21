package com.ashim.pricedrop.repository;

import com.ashim.pricedrop.model.PriceAlert;

import java.util.*;
import java.util.stream.Collectors;

public class InMemoryPriceAlertRepository implements PriceAlertRepository {

    private final Map<UUID, PriceAlert> alerts = new HashMap<>();


    @Override
    public PriceAlert save(PriceAlert alert) {
        if(alert == null) throw new NullPointerException("PriceAlert cannot be null");
        alerts.put(alert.getId(), alert);
        return alert;
    }

    @Override
    public Optional<PriceAlert> findById(UUID id) {
        return Optional.ofNullable(alerts.get(id));
    }

    @Override
    public List<PriceAlert> findAll() {
        return new ArrayList<>(alerts.values());
    }

    @Override
    public List<PriceAlert> findByUserId(UUID userId) {
//        return alerts.values().stream()
//                .filter(alert -> alert.getUser().getUserId().equals(userId))
//                .collect(Collectors.toList());
        return alerts.values().stream()
                .filter(alert -> alert.getUser().getUserId().equals(userId))
                .toList(); // Prefer .toList() in Java 21 for concise, unmodifiable result lists:
    } // converts to stream and filters by userId, then collects to list

    @Override
    public List<PriceAlert> findByProductId(UUID productId) {
        return alerts.values().stream()
                .filter(alert -> alert.getProduct().getId().equals(productId))
                .collect(Collectors.toList());
    } // converts to stream and filters by productId, then collects to list
}
