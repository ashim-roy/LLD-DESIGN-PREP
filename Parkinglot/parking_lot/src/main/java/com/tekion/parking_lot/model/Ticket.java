package com.tekion.parking_lot.model;

import com.tekion.parking_lot.enums.SpotType;
import com.tekion.parking_lot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ticket {

    private String ticketId;
    private String spotId;
    private Vehicle vehicle;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private long fee;
}
