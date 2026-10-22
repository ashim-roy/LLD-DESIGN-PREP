package com.ashim.elevator.model;

import com.ashim.elevator.enums.Direction;

public class ElevatorRequest {
    //Represents a request from a floor to a destination
    /*
     ElevatorRequest Class Requirements:
     - Purpose: Represents a passenger elevator request specifying where the passenger is waiting, where they want to go, and their travel direction.
     - Fields (private final):
     - sourceFloor (int): Floor where the passenger is waiting.
     - destinationFloor (int): Floor the passenger wants to reach.
     - direction (Direction): UP or DOWN, derived automatically.

     - Validation Rules:
     - Reject requests where source and destination floors are the same.
     - Reject negative floor numbers (assuming ground floor is 0).

     - Design Notes:
     - Direction is derived automatically inside the constructor (Destination > Source = UP, Destination < Source = DOWN) to prevent inconsistent states (e.g., source 2, destination 8, direction DOWN).
     - Provide getters for all three fields.
     */

    private final int sourceFloor;
    private final int destinationFloor;
    private final Direction direction;  // ENUM hai apne pas

    public ElevatorRequest(int sourceFloor, int destinationFloor) {
        if(sourceFloor < 0 || destinationFloor < 0 || sourceFloor == destinationFloor) {
            throw new IllegalArgumentException("invalid source or destination floor input. fuck off");
        }
//        if(destinationFloor > sourceFloor) {
//            direction = "up";
//        }else {
//            direction = "down";
//        }

        if (destinationFloor > sourceFloor) {
            this.direction = Direction.UP;
        } else {
            this.direction = Direction.DOWN;
        }
        // Because direction is final, assigning it in either branch is valid. You don't need to assign it again at the end of the constructor.

        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;

    }

    public int getSourceFloor() {
        return sourceFloor;
    }
    public int getDestinationFloor() {
        return destinationFloor;
    }
    public Direction getDirection() {
        return direction;
    }

}
