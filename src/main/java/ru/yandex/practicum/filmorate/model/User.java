package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class User {
    private Long id;
    @Email
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

    public void removeFriend(long id) {
        friends.remove(id);
    }

}
