package com.ashim.elevator.service;

import com.ashim.elevator.enums.ElevatorState;
import com.ashim.elevator.model.Elevator;
import com.ashim.elevator.model.ElevatorRequest;

import java.util.List;

public class NearestElevatorSelectionStrategy implements ElevatorSelectionStrategy {
    @Override
    public Elevator selectElevator(List<Elevator> elevators, ElevatorRequest request) {
        // Choose the idle elevator closest to the passenger's source floor.
        // If two elevators are equally close, either one is acceptable for now. If no elevator is idle, return null for the MVP; we'll improve this behavior later.
        int sourceFloor = request.getSourceFloor();
        int nearestDistance = Integer.MAX_VALUE;
        Elevator nearestElevator = null;

        for(Elevator elevator : elevators){
            if(elevator.getState()== ElevatorState.IDLE){
                int currentFloor = elevator.getCurrentFloor();
                int distance = Math.abs(currentFloor - sourceFloor);

                if (distance < nearestDistance) {
                    nearestDistance = distance;
                    nearestElevator = elevator;
                }
            }
        }
        return nearestElevator;

    }
}
/*
We should eventually handle two edge cases:
- An empty or null elevator list.
- No available idle elevator.
For now, returning null for no idle elevator is consistent with our MVP. Later, ElevatorSystem can decide whether to queue the request or report that no elevator is available.
 */
