package com.ashim.elevator.enums;

public enum Direction {
    UP,
    DOWN,
    IDLE
}
/*
You already created Direction.java, so let's use it rather than storing "up" and "down" as strings.

Why?
- Prevents invalid values such as "UPP" or "upwards".
- Keeps direction comparisons type-safe.
- Reuses the enum consistently across the system.
 */
