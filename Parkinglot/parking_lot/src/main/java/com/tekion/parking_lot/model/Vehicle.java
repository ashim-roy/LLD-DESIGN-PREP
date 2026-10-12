package com.tekion.parking_lot.model;

import com.tekion.parking_lot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    private String numberPlate;
    private VehicleType vehicleType;
}
