package com.ashim.elevator.service;

import com.ashim.elevator.enums.Direction;
import com.ashim.elevator.enums.ElevatorState;
import com.ashim.elevator.model.Elevator;
import com.ashim.elevator.model.ElevatorRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorControllerTest {

    private ElevatorController controller;

    // @BeforeEach runs this method before every @Test method.
    // We create a fresh NearestElevatorSelectionStrategy for each test setup.
    // The strategy decides which idle elevator is closest to the passenger's source floor.
    // We inject the strategy into ElevatorController through its constructor.
    // This keeps the tests independent and avoids sharing controller state between tests.
    @BeforeEach
    void setUp() {
        ElevatorSelectionStrategy strategy =
                new NearestElevatorSelectionStrategy();

        controller = new ElevatorController(strategy);
    }


    @Test
    void shouldMoveElevatorUpToSourceAndDestination() {

        // GIVEN: Arrange the test data.
        // Create an elevator with ID 1, initially located at floor 0.
        Elevator elevator = new Elevator(1, 0);

        // List.of() creates an immutable list containing this elevator.
        // The list structure cannot change, but the Elevator object can still be modified.
        List<Elevator> elevators = List.of(elevator);

        // Create a passenger request to travel from floor 3 to floor 7.
        // The elevator must first travel from floor 0 to floor 3,
        // pick up the passenger, and then travel to floor 7.
        ElevatorRequest request = new ElevatorRequest(3, 7);


        // WHEN: Execute the behavior being tested.
        // The controller selects an elevator and simulates the complete journey:
        // move to the source floor, open/close doors, move to the destination,
        // and finish the journey.
        controller.handleRequest(elevators, request);


        // THEN: Verify the expected results.
        // The elevator should finish at the passenger's destination floor (7).
        assertEquals(7, elevator.getCurrentFloor());

        // After completing the journey, the elevator should have no active direction.
        assertEquals(Direction.IDLE, elevator.getDirection());

        // The elevator should be idle after dropping off the passenger.
        assertEquals(ElevatorState.IDLE, elevator.getState());
    }



    @Test
    void shouldMoveElevatorDownToSourceAndDestination() {

        // GIVEN: Arrange the initial state and passenger request.
        // Create an elevator with ID 1, initially located at floor 8.
        Elevator elevator = new Elevator(1, 8);

        // Provide the controller with a list containing this elevator.
        List<Elevator> elevators = List.of(elevator);

        // The passenger wants to travel from floor 5 to floor 2.
        // The elevator starts at floor 8, so it must first move DOWN to floor 5,
        // pick up the passenger, and continue DOWN to floor 2.
        ElevatorRequest request = new ElevatorRequest(5, 2);


        // WHEN: Ask the controller to handle the passenger request.
        // It selects the elevator and simulates movement, pickup, and drop-off.
        controller.handleRequest(elevators, request);


        // THEN: Verify the final state after the journey.
        // The elevator should finish at the destination floor, which is floor 2.
        assertEquals(2, elevator.getCurrentFloor());

        // The elevator should no longer have an active travel direction.
        assertEquals(Direction.IDLE, elevator.getDirection());

        // The elevator should be idle after completing the journey.
        assertEquals(ElevatorState.IDLE, elevator.getState());
    }



    @Test
    void shouldSelectNearestIdleElevator() {

        // GIVEN: Create two idle elevators at different floors.
        // Elevator 1 is at floor 0, which is 5 floors away from the passenger.
        Elevator farElevator = new Elevator(1, 0);

        // Elevator 2 is at floor 4, only 1 floor away from the passenger.
        Elevator nearestElevator = new Elevator(2, 4);

        // Give both elevators to the controller through the elevator list.
        List<Elevator> elevators =
                List.of(farElevator, nearestElevator);

        // The passenger requests a journey from floor 5 to floor 8.
        // The selection strategy should choose Elevator 2 because it is closer.
        ElevatorRequest request = new ElevatorRequest(5, 8);


        // WHEN: Handle the request.
        // The controller delegates elevator selection to the configured strategy.
        // The strategy should select Elevator 2, which starts at floor 4.
        // That elevator moves to floor 5, picks up the passenger,
        // and then travels to floor 8.
        controller.handleRequest(elevators, request);


        // THEN: Verify that the correct elevator was selected.
        // Elevator 1 should remain at floor 0 because it was not selected.
        assertEquals(0, farElevator.getCurrentFloor());

        // Elevator 2 should complete the passenger's journey at floor 8.
        assertEquals(8, nearestElevator.getCurrentFloor());

        // The selected elevator should be idle after completing the journey.
        assertEquals(ElevatorState.IDLE, nearestElevator.getState());
    }



    @Test
    void shouldThrowExceptionWhenNoElevatorIsAvailable() {

        // GIVEN: Create an empty list of elevators.
        // No elevator is available to serve the passenger's request.
        List<Elevator> elevators = new ArrayList<>();

        // Create a valid request to travel from floor 2 to floor 6.
        ElevatorRequest request = new ElevatorRequest(2, 6);


        // WHEN & THEN: Verify the expected exception.
        // The controller asks the selection strategy to select an elevator.
        // Since the list is empty, the strategy returns null.
        // The controller detects the missing elevator and throws IllegalStateException.
        // assertThrows() passes only if the expected exception is thrown.
        assertThrows(
                IllegalStateException.class,
                () -> controller.handleRequest(elevators, request)
        );
    }



    @Test
    void shouldFinishJourneyWithDoorsClosedAndElevatorIdle() {

        // GIVEN: Create an elevator at floor 2.
        Elevator elevator = new Elevator(1, 2);

        // Provide the controller with a list containing this elevator.
        List<Elevator> elevators = List.of(elevator);

        // The passenger is already at floor 2 and wants to travel to floor 6.
        // The elevator does not need to travel to pick up the passenger.
        ElevatorRequest request = new ElevatorRequest(2, 6);


        // WHEN: Execute the complete passenger journey.
        // The controller opens and closes the doors at floor 2,
        // moves the elevator to floor 6, and handles passenger drop-off.
        controller.handleRequest(elevators, request);


        // THEN: Verify the elevator's final position and state.
        // The elevator should finish at the passenger's destination, floor 6.
        assertEquals(6, elevator.getCurrentFloor());

        // The elevator should be IDLE after completing the journey.
        // This represents that it is no longer actively moving or serving this request.
        assertEquals(ElevatorState.IDLE, elevator.getState());

        // The direction should also be IDLE because the journey is complete.
        assertEquals(Direction.IDLE, elevator.getDirection());
    }

}
