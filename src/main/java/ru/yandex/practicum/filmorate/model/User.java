package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Email;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Builder
@Data
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

    public Long removeFriend(long id) {
        if (friends.contains(id)) {
            friends.remove(id);
            return id;
        } else {
            return 0L;
        }
    }

}
