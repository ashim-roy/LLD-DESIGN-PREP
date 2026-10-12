package com.tekion.parking_lot.strategy;

import com.tekion.parking_lot.model.Ticket;

import java.time.LocalDateTime;

public interface PricingStrategy {

    long calculateFee(Ticket ticket, LocalDateTime exitTime);
}
