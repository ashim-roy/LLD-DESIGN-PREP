package com.tekion.parking_lot.model;


import com.tekion.parking_lot.enums.SpotType;
import com.tekion.parking_lot.enums.VehicleType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSpot {

    private String spotId;
    private SpotType spotType;
    private Vehicle parkedVehicle;


    // TODO what does it do
    // What it does: Checks whether a vehicle is currently parked in this spot.
    //- parkedVehicle == null means no vehicle is parked, so it returns true.
    //- If parkedVehicle contains a vehicle, it returns false.

    public boolean isAvailable() {
        return parkedVehicle == null;
    }

    // park(Vehicle vehicle) — Park a vehicle
    public boolean park(Vehicle vehicle) {
        if (!isAvailable()) {
            return false; // If occupied, parking fails.
        }
        this.parkedVehicle = vehicle; // Stores the vehicle in this parking spot.
        return true; // Confirms that parking succeeded.
    }

    public void release() {
        this.parkedVehicle = null; //Remove the vehicle from the spot
    }
}