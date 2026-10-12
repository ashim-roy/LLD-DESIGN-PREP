package com.tekion.parking_lot.strategy;


import com.tekion.parking_lot.enums.SpotType;
import com.tekion.parking_lot.enums.VehicleType;
import com.tekion.parking_lot.model.Ticket;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class HourlyPricingStrategy implements PricingStrategy {

//    private final Map<VehicleType, Long> hourlyRates = Map.of(
//            VehicleType.BIKE, 10L,
//            VehicleType.CAR, 20L,
//            VehicleType.TRUCK, 50L
//    );

    private final Map<VehicleType, Long> hourlyRates = createRates();

    private Map<VehicleType, Long> createRates() {
        Map<VehicleType, Long> rates = new HashMap<>();

        rates.put(VehicleType.BIKE, 10L);
        rates.put(VehicleType.CAR, 20L);
        rates.put(VehicleType.TRUCK, 50L);

        return rates;
    }

    @Override
    public long calculateFee(Ticket ticket, LocalDateTime exitTime) {
        long minutes = Duration.between(ticket.getEntryTime(), exitTime).toMinutes();

        // Integer trick for ceil(minutes / 60): handles 0 mins up to 60 mins -> 1 hr
        long hours = Math.max(1, (minutes + 59) / 60);

        return hours * hourlyRates.get(ticket.getVehicle().getVehicleType());
    }
}