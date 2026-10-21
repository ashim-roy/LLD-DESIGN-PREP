package com.ashim.pricedrop.repository;

import com.ashim.pricedrop.model.User;

import java.util.*;

public class InMemoryUserRepository implements UserRepository {

    private final Map<UUID, User> users = new HashMap<>();

    @Override
    public User save(User user) {
        if (user == null) {
            throw new NullPointerException("User cannot be null");
        }

        users.put(user.getUserId(), user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(users.values());
    }
}
