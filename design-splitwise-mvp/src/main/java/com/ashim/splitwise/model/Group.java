package com.ashim.splitwise.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Group {
    private final UUID groupId;
    private final String groupName;
    private final List<User> members = new ArrayList<>();
    private final User createdBy;

    public Group(String groupName, User createdBy) {
        if (groupName == null || groupName.isBlank()) {
            throw new IllegalArgumentException("Group name cannot be null or blank");
        }

        if (createdBy == null) {
            throw new IllegalArgumentException("Group createdBy cannot be null");
        }

        this.groupId = UUID.randomUUID();
        this.groupName = groupName;
        this.createdBy = createdBy;

       // this.members = new ArrayList<>();
        this.members.add(createdBy);
    }

    public UUID getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public Optional<List<User>> getMembers() {
        return Optional.ofNullable(members);
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void addMember(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if(members.contains(user)) {
            throw new IllegalArgumentException("User is already a member of the group");
        }
        members.add(user);
    }

    public void removeMember(User user) {
        if (user == null) {
            throw new IllegalArgumentException("User cannot be null");
        }
        if(!members.contains(user)) {
            throw new IllegalArgumentException("User is not a member of the group");
        }
        if(user.equals(createdBy)) { // Creator check — use user.equals(createdBy) instead of user == createdBy. == checks object identity; equals() checks your User ID.
            throw new IllegalArgumentException("Cannot remove the creator of the group");
        }
        members.remove(user);
    }

}
