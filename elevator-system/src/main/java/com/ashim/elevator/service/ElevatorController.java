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

    }

    public void handleRequest(
            List<Elevator> elevators,
            ElevatorRequest request) {
        // TODO: implement
    }

}
