package com.ashim.elevator.service;

import com.ashim.elevator.model.Elevator;
import com.ashim.elevator.model.ElevatorRequest;

import java.util.ArrayList;
import java.util.List;

public class ElevatorSystem {
    // Initializes and coordinates the building's elevators
    private final int minFloor;
    private final int maxFloor;
    private final List<Elevator> elevators;
    private final ElevatorController elevatorController;

    public ElevatorSystem(int minFloor, int maxFloor, List<Elevator> elevators, ElevatorController elevatorController) {

        if (minFloor > maxFloor) {
            throw new IllegalArgumentException("minFloor must be less than maxFloor");
        }
//        if(elevators.isEmpty() || elevators== null ) {
//            throw new IllegalArgumentException("elevators cannot be empty");
//        }
        // Java evaluates conditions from left to right. If elevators is null, isEmpty() throws a NullPointerException before the null check happens.
        if (elevators == null || elevators.isEmpty()) {
            throw new IllegalArgumentException(
                    "Elevators cannot be null or empty"
            );
        }

        if(elevatorController == null){
            throw new IllegalArgumentException("elevatorController cannot be null");
        }
        this.minFloor = minFloor;
        this.maxFloor = maxFloor;
        //this.elevators = elevators; // That means the caller could modify the original list after constructing ElevatorSystem. For this MVP, I'd recommend making a defensive copy:
        this.elevators = new ArrayList<>(elevators); // defensive copy fo rcolelction
        this.elevatorController = elevatorController;
    }

    /*
    Next task: Implement requestElevator()
        Once those fixes are done, add a public method to ElevatorSystem that accepts an ElevatorRequest.

        Its responsibilities are:
        1. Reject a null request.
        2. Validate that both the source and destination floors are within minFloor and maxFloor, inclusive.
        3. Delegate valid requests to elevatorController.handleRequest(elevators, request).
        Start with the method signature:
     */

    public void requestElevator(ElevatorRequest request){
        if(request == null){
            throw new IllegalArgumentException("request cannot be null");
        }

        // Validate that both the source and destination floors are within minFloor and maxFloor, inclusive.
        // This supports buildings with either a ground floor of 0 or basement floors such as -1 and -2.
        if (request.getSourceFloor() < minFloor
                || request.getSourceFloor() > maxFloor
                || request.getDestinationFloor() < minFloor
                || request.getDestinationFloor() > maxFloor) {
            throw new IllegalArgumentException(
                    "Source and destination floors must be within bounds"
            );
        }

        // Delegate valid requests to elevatorController.handleRequest(elevators, request)
        elevatorController.handleRequest(elevators, request);
        // The system validates the incoming request and delegates the actual journey to the controller. That's the separation of responsibilities we want.

    }
    
    
}
