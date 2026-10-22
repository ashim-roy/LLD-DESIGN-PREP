package com.ashim.elevator.service;

import com.ashim.elevator.model.Elevator;
import com.ashim.elevator.model.ElevatorRequest;

import java.util.List;

public interface ElevatorSelectionStrategy {
    // Contract for choosing an elevator
    Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request);
}

/*
Why an interface? We can later implement different strategies—such as choosing the nearest idle elevator
or preferring an elevator already moving in the passenger's direction—without changing ElevatorSystem.
 */
