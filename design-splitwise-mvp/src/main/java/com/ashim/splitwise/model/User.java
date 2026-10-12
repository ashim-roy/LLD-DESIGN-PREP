package com.ashim.splitwise.model;

import java.util.UUID;

public class User {
    private final UUID id;
    private final String name;
    private final String email;

    public User(String name, String email) {
        // UUID id = UUID.randomUUID();

        // check email, name arenot null or blank
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("User name cannot be null or blank");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("User email cannot be null or blank");
        }

        this.id = UUID.randomUUID();
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        User other = (User) obj;

        return this.id.equals(other.id);
    }

    //Override hashCode() using id
    public int hashCode() {
        return id.hashCode();
    }
    //You could use Objects.hash(id). Objects.hash(id) also works, but your version is simpler and avoids unnecessary overhead.


    @Override
    public String toString() { // useful for debugging.
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

}
