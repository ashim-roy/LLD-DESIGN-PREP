package com.ashim.elevator.service;


import com.ashim.elevator.model.Elevator;
import com.ashim.elevator.model.ElevatorRequest;

import java.util.List;

/*
For our MVP, the controller needs to:
1. Accept an ElevatorSelectionStrategy through its constructor.
2. Select an elevator for a given request.
3. Move the selected elevator toward the passenger's source floor.
4. Open the doors at the source floor, then close them.
5. Move toward the destination floor.
6. Open the doors at the destination, then finish in the IDLE state.
 */
public class ElevatorController {
    // Routes requests to elevators
    private final ElevatorSelectionStrategy selectionStrategy;


    public ElevatorController(ElevatorSelectionStrategy selectionStrategy) {
        // TODO: validate and assign selectionStrategy
        if (selectionStrategy == null) {
            throw new IllegalArgumentException(
                    "Selection strategy cannot be null"
            );
        }
        this.selectionStrategy = selectionStrategy;
    }

    public void handleRequest(
            List<Elevator> elevators,
            ElevatorRequest request) {
        // TODO: implement
        Elevator nearestElevator = selectionStrategy.selectElevator(elevators, request);

        if (nearestElevator == null) {
            throw new IllegalStateException("No elevator available");
        }

        int sourceFloor = request.getSourceFloor();
        // int currentFloor = nearestElevator.getCurrentFloor();
        //request.getDirection(sourceFloor, currentFloor);


        // Move the selected elevator toward the passenger's source floor.
        while (nearestElevator.getCurrentFloor() != sourceFloor) {
            if (nearestElevator.getCurrentFloor() < sourceFloor) {
                nearestElevator.moveUp();
            } else {
                nearestElevator.moveDown();
            }
        }
        // Open the doors at the source floor, then close them.
        nearestElevator.openDoor();
        nearestElevator.closeDoor();

        //  Move toward the destination floor.
        int destinationFloor = request.getDestinationFloor();
        /*
        Use the same movement logic you wrote for the source floor, but compare against destinationFloor.
        - If the elevator is below the destination, call moveUp().
        - Otherwise, call moveDown().
        - Repeat until the elevator reaches the destination.
         */
        while(destinationFloor != nearestElevator.getCurrentFloor()) {
            if (nearestElevator.getCurrentFloor() < destinationFloor) {
                nearestElevator.moveUp();
            } else {
                nearestElevator.moveDown();
            }
        }
        // Once the elevator reaches the destination, add:
        nearestElevator.openDoor();
        nearestElevator.closeDoor();
        nearestElevator.stop();

    }

}
