package com.tekion.parking_lot.service;



import com.tekion.parking_lot.enums.SpotType;
import com.tekion.parking_lot.enums.VehicleType;
import com.tekion.parking_lot.model.ParkingSpot;
import com.tekion.parking_lot.model.Ticket;
import com.tekion.parking_lot.model.Vehicle;
import com.tekion.parking_lot.strategy.PricingStrategy;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service // Tells Spring that this class is a service component managed by Spring.
public class ParkingLotService {

    // Stores all parking spots using spot ID as the key.
    // LinkedHashMap preserves the insertion order of parking spots.
    @Getter // Lombok generates a getter method for the spots map.
    private final Map<String, ParkingSpot> spots = new LinkedHashMap<>();

    // Stores currently active parking tickets.
    // Key = ticket ID, Value = Ticket object.
    private final Map<String, Ticket> activeTickets = new HashMap<>();

    // Tracks which ticket belongs to each currently parked vehicle.
    // Key = vehicle number plate, Value = ticket ID.
    private final Map<String, String> activeVehicles = new HashMap<>();

    // Strategy used to calculate the parking fee.
    private final PricingStrategy pricingStrategy;

    // Constructor injection: Spring provides the PricingStrategy implementation.
    public ParkingLotService(PricingStrategy pricingStrategy) {
        this.pricingStrategy = pricingStrategy;
    }

    // Adds a new parking spot to the parking lot.
    public void addSpot(ParkingSpot spot) {

        // Prevents two parking spots from having the same ID.
        if (spots.containsKey(spot.getSpotId())) {
            // Throws an exception if the spot ID already exists.
            throw new IllegalArgumentException(
                    "Duplicate spot ID: " + spot.getSpotId()
            );
        }

        // Stores the spot using its ID as the map key.
        spots.put(spot.getSpotId(), spot);
    }

    // Determines which spot type is required for a given vehicle type.
    private SpotType getRequiredSpotType(VehicleType vehicleType) {

        // Maps each vehicle type to its compatible parking spot type.
        return switch (vehicleType) {
            case BIKE -> SpotType.SMALL;   // Bikes require small spots.
            case CAR -> SpotType.MEDIUM;   // Cars require medium spots.
            case TRUCK -> SpotType.LARGE;  // Trucks require large spots.
        };
    }

    // Parks a vehicle and generates a parking ticket.
    public Ticket parkVehicle(Vehicle vehicle) {

        // Retrieves the vehicle's registration number.
        String plate = vehicle.getNumberPlate();

        // Checks whether this vehicle is already parked.
        if (activeVehicles.containsKey(plate)) {
            // Prevents the same vehicle from being parked twice.
            throw new IllegalStateException(
                    "Vehicle is already parked: " + plate
            );
        }

        // Finds the spot type compatible with this vehicle.
        SpotType required = getRequiredSpotType(vehicle.getVehicleType());

        // Iterates through all parking spots in insertion order.
        for (ParkingSpot spot : spots.values()) {

            // Finds the first spot with the required type that is empty.
            if (spot.getSpotType() == required && spot.isAvailable()) {

                // Parks the vehicle in the selected spot.
                spot.park(vehicle);

                // Creates a new ticket for this parking session.
                Ticket ticket = new Ticket(
                        UUID.randomUUID().toString(), // Generates a unique ticket ID.
                        spot.getSpotId(),             // Records the allocated spot ID.
                        vehicle,                      // Records the parked vehicle.
                        LocalDateTime.now(),          // Records the entry time.
                        null,                         // Exit time is unknown at entry.
                        0                             // Fee is zero until exit.
                );

                // Saves the ticket as an active parking ticket.
                activeTickets.put(ticket.getTicketId(), ticket);

                // Records the ticket associated with this vehicle's plate.
                activeVehicles.put(plate, ticket.getTicketId());

                // Returns the generated ticket to the caller.
                return ticket;
            }
        }

        // Reaches here if no compatible spot is available.
        throw new IllegalStateException(
                "No available spot for " + vehicle.getVehicleType()
        );
    }

    // Removes a vehicle using its ticket ID and returns the parking fee.
    public long unparkVehicle(String ticketId) {

        // Finds the active ticket using the supplied ticket ID.
        Ticket ticket = activeTickets.get(ticketId);

        // A null result means the ticket is invalid or no longer active.
        if (ticket == null) {
            // Rejects invalid tickets and tickets already used for exit.
            throw new IllegalArgumentException(
                    "Invalid or already used ticket: " + ticketId
            );
        }

        // Records the current time as the vehicle's exit time.
        LocalDateTime exitTime = LocalDateTime.now();

        // Calculates the fee using the configured pricing strategy.
        long fee = pricingStrategy.calculateFee(ticket, exitTime);

        // Retrieves the parking spot allocated to this ticket.
        ParkingSpot spot = spots.get(ticket.getSpotId());

        // Updates the ticket with the exit time.
        ticket.setExitTime(exitTime);

        // Stores the calculated fee in the ticket.
        ticket.setFee(fee);

        // Removes the vehicle from the spot, making it available again.
        spot.release();

        // Removes the ticket from the collection of active tickets.
        activeTickets.remove(ticketId);

        // Removes the vehicle from the collection of currently parked vehicles.
        activeVehicles.remove(ticket.getVehicle().getNumberPlate());

        // Returns the parking fee to the caller.
        return fee;
    }

    // Returns the total number of currently available parking spots.
    public long getAvailableSpots() {

        // Gets all spots, keeps only those that are empty,
        // and counts how many remain.
        return spots.values().stream()
                .filter(ParkingSpot::isAvailable)
                .count();
    }
}
