package com.ashim.elevator.model;

import com.ashim.elevator.enums.Direction;
import com.ashim.elevator.enums.ElevatorState;

/*
Elevator Class Requirements:
- Purpose: Represents the physical elevator's current condition and state, keeping responsibilities separate from passenger requests (ElevatorRequest).
- Fields:
  - elevatorId (int): Unique identifier for the elevator.
  - currentFloor (int): The floor where the elevator is currently located.
  - direction (Direction): Current travel direction (UP, DOWN, etc.).
  - state (ElevatorState): Current operating state (IDLE, MOVING, DOOR_OPEN, etc.).

- Implementation Steps:
  - Create the constructor and getters first.
  - Implement movement and operation methods: moveUp(), moveDown(), and openDoor().

- Design Notes:
- One design decision to keep in mind: ElevatorRequest represents what a passenger wants;
Elevator represents the physical elevator's current condition. Keeping those responsibilities separate will make the system easier to extend.
  - Separating physical elevator status from passenger requests makes the system easier to extend.
 */
public class Elevator {
    // Maintains floor, direction, state, and movement
    private final int elevatorId;
    private int currentFloor;
    private Direction direction;
    private ElevatorState state;

    public Elevator(int elevatorId, int currentFloor) {

        if(elevatorId <= 0){
            throw new IllegalArgumentException("Elevator id must be greater than 0");
        }
        if(currentFloor < 0 ) {
            throw new IllegalArgumentException("Current floor cannot be negative");
        }
//        if(direction == null) {
//            throw new IllegalArgumentException("invalid input");
//        }
        // Your constructor doesn't accept a direction parameter. You're setting the direction internally:
        this.elevatorId = elevatorId;
        this.currentFloor = currentFloor;

        this.direction = Direction.IDLE;
        this.state = ElevatorState.IDLE;

    }
    public int getElevatorId() {
        return elevatorId;
    }
    public int getCurrentFloor() {
        return currentFloor;
    }
    public Direction getDirection() {
        return direction;
    }
    public ElevatorState getState() {
        return state;
    }

    /*
    Package: com.ashim.elevator.model

Elevator Movement Requirements (moveUp / moveDown):
- Purpose: Implement movement methods for the Elevator class while managing floor boundaries.
- Design Decision:
  - Keep the Elevator class independent of the building's total floor limits (avoid passing maxFloor into the Elevator constructor).
  - Let the ElevatorSystem handle boundary validation instead.
- Design Notes:
  - Separating movement boundary validation into the controller/system achieves cleaner separation of responsibilities, which is ideal for LLD interviews.
     */

    public void moveUp(){
        /*
        Increase currentFloor by one.
        Set direction to Direction.UP.
        Set state to ElevatorState.MOVING.
         */
        currentFloor++;
        direction = Direction.UP;
        state = ElevatorState.MOVING;
    }

    public void  moveDown(){
        currentFloor--;
        direction = Direction.DOWN;
        state = ElevatorState.MOVING;
    }

    public void stop(){
        direction = Direction.IDLE;
        state = ElevatorState.IDLE;
    }

    public void openDoor(){
        state = ElevatorState.DOOR_OPEN;
        direction = Direction.IDLE;

    }

    public void closeDoor(){
        state = ElevatorState.IDLE;
    }

}
