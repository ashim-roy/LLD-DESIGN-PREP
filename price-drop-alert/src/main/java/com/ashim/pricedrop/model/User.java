package com.ashim.pricedrop.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    private final UUID userId;
    private final String username;
    private final String email;

    public User(String username, String email) {

//        if(username == null) throw new NullPointerException();
//        if(email == null) throw new NullPointerException();
        // NullPointerException is acceptable for null arguments, but use Objects.requireNonNull() for cleaner validation.

//        if(username.isEmpty()) throw new IllegalArgumentException("Username cannot be empty");
//        if(email.isEmpty()) throw new IllegalArgumentException("Email cannot be empty");

        // Use isBlank() instead of isEmpty() for username and email, so whitespace-only values are rejected.
//        if(username.isBlank()) throw new IllegalArgumentException("Username cannot be empty or whitespace");
//        if(email.isBlank()) throw new IllegalArgumentException("Email cannot be empty or whitespace");
//
//        this.userId = UUID.randomUUID();
//        this.username = username;
//        this.email = email;
        this.username = Objects.requireNonNull(username, "Username cannot be null");
        this.email = Objects.requireNonNull(email, "Email cannot be null");

        if (username.isBlank()) {
            throw new IllegalArgumentException("Username cannot be blank");
        }
        if (email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        this.userId = UUID.randomUUID();
    }

    public UUID getUserId() {
        return this.userId;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

}

/*
equals() and hashCode()? Not necessary yet. User has a UUID identity, so we can implement them if we need identity-based equality or use users in sets/maps.
toString()? Optional, useful for debugging, but not required for this MVP.
 */
