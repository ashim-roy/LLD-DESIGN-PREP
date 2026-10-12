package com.tekion.parking_lot;

import com.tekion.parking_lot.enums.SpotType;
import com.tekion.parking_lot.enums.VehicleType;
import com.tekion.parking_lot.model.ParkingSpot;
import com.tekion.parking_lot.model.Ticket;
import com.tekion.parking_lot.model.Vehicle;
import com.tekion.parking_lot.service.ParkingLotService;
import com.tekion.parking_lot.strategy.PricingStrategy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {

    @Mock
    private PricingStrategy pricingStrategy;

    private ParkingLotService parkingLotService;

    @BeforeEach
    void setUp() {
        parkingLotService = new ParkingLotService(pricingStrategy);

        parkingLotService.addSpot(
                new ParkingSpot("S1", SpotType.SMALL, null)
        );

        parkingLotService.addSpot(
                new ParkingSpot("M1", SpotType.MEDIUM, null)
        );

        parkingLotService.addSpot(
                new ParkingSpot("M2", SpotType.MEDIUM, null)
        );

        parkingLotService.addSpot(
                new ParkingSpot("L1", SpotType.LARGE, null)
        );
    }

    @Test
    void shouldParkCarInAvailableMediumSpot() {
        Vehicle car = new Vehicle("KA01AB1234", VehicleType.CAR);

        Ticket ticket = parkingLotService.parkVehicle(car);

        assertNotNull(ticket);
        assertEquals("M1", ticket.getSpotId());
        assertEquals(car, ticket.getVehicle());
        assertNotNull(ticket.getEntryTime());

        assertEquals(3, parkingLotService.getAvailableSpots());
    }

    @Test
    void shouldParkTwoCarsInDifferentSpots() {
        Vehicle car1 = new Vehicle("KA01AB1111", VehicleType.CAR);
        Vehicle car2 = new Vehicle("KA01AB2222", VehicleType.CAR);

        Ticket ticket1 = parkingLotService.parkVehicle(car1);
        Ticket ticket2 = parkingLotService.parkVehicle(car2);

        assertEquals("M1", ticket1.getSpotId());
        assertEquals("M2", ticket2.getSpotId());
        assertNotEquals(ticket1.getTicketId(), ticket2.getTicketId());

        assertEquals(2, parkingLotService.getAvailableSpots());
    }

    @Test
    void shouldFailWhenNoCompatibleSpotIsAvailable() {
        Vehicle car1 = new Vehicle("KA01AB1111", VehicleType.CAR);
        Vehicle car2 = new Vehicle("KA01AB2222", VehicleType.CAR);
        Vehicle car3 = new Vehicle("KA01AB3333", VehicleType.CAR);

        parkingLotService.parkVehicle(car1);
        parkingLotService.parkVehicle(car2);

        assertThrows(
                IllegalStateException.class,
                () -> parkingLotService.parkVehicle(car3)
        );

        assertEquals(2, parkingLotService.getAvailableSpots());
    }

    @Test
    void shouldPreventSameVehicleFromParkingTwice() {
        Vehicle car = new Vehicle("KA01AB1234", VehicleType.CAR);

        parkingLotService.parkVehicle(car);

        assertThrows(
                IllegalStateException.class,
                () -> parkingLotService.parkVehicle(car)
        );

        assertEquals(3, parkingLotService.getAvailableSpots());
    }

    @Test
    void shouldCalculateFeeAndReleaseSpotWhenUnparking() {
        Vehicle car = new Vehicle("KA01AB1234", VehicleType.CAR);
        Ticket ticket = parkingLotService.parkVehicle(car);

        when(pricingStrategy.calculateFee(
                eq(ticket), any(java.time.LocalDateTime.class)
        )).thenReturn(40L);

        long fee = parkingLotService.unparkVehicle(ticket.getTicketId());

        assertEquals(40L, fee);
        assertEquals(40L, ticket.getFee());
        assertNotNull(ticket.getExitTime());

        assertEquals(4, parkingLotService.getAvailableSpots());

        verify(pricingStrategy).calculateFee(
                eq(ticket), any(java.time.LocalDateTime.class)
        );
    }

    @Test
    void shouldRejectInvalidTicket() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parkingLotService.unparkVehicle("INVALID-TICKET")
        );
    }

    @Test
    void shouldRejectTicketThatWasAlreadyUsed() {
        Vehicle car = new Vehicle("KA01AB1234", VehicleType.CAR);
        Ticket ticket = parkingLotService.parkVehicle(car);

        when(pricingStrategy.calculateFee(
                eq(ticket), any(java.time.LocalDateTime.class)
        )).thenReturn(20L);

        parkingLotService.unparkVehicle(ticket.getTicketId());

        assertThrows(
                IllegalArgumentException.class,
                () -> parkingLotService.unparkVehicle(ticket.getTicketId())
        );
    }

    @Test
    void shouldAllowVehicleToParkAgainAfterUnparking() {
        Vehicle car = new Vehicle("KA01AB1234", VehicleType.CAR);
        Ticket firstTicket = parkingLotService.parkVehicle(car);

        when(pricingStrategy.calculateFee(
                eq(firstTicket), any(java.time.LocalDateTime.class)
        )).thenReturn(20L);

        parkingLotService.unparkVehicle(firstTicket.getTicketId());

        Ticket secondTicket = parkingLotService.parkVehicle(car);

        assertNotNull(secondTicket);
        assertNotEquals(
                firstTicket.getTicketId(),
                secondTicket.getTicketId()
        );
        assertEquals("M1", secondTicket.getSpotId());
    }

    @Test
    void shouldRejectDuplicateSpotId() {
        assertThrows(
                IllegalArgumentException.class,
                () -> parkingLotService.addSpot(
                        new ParkingSpot("M1", SpotType.MEDIUM, null)
                )
        );
    }
}
