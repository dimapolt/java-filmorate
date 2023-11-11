package ru.yandex.practicum.filmorate.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.*;

@Data
public class User {
    private Long id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Long> friends;

    public User() {
        friends = new LinkedHashSet<>();
    }

    public List<Long> getFriendsId() {
        return new ArrayList<>(friends);
    }

    public void addFriend(Long friendId) {
        friends.add(friendId);
    }

    public Long removeFriend(long id) {
        if (friends.contains(id)) {
            friends.remove(id);
            return id;
        } else {
            return 0L;
        }
    }

}
