package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getAllUsers();

    ResponseEntity<User> createUser(User user);

    ResponseEntity<User> updateUser(User user);

    void deleteUser(Long id);

    User getUserById(Long id);

    boolean removeFriend(Long id, Long friendId);
}
