package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<User> getAllUsers() {
        log.info("Получен запрос на список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        Optional<User> userO = users.values()
                .stream()
                .filter(u -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail()))
                .findFirst();

        FilmRateValidator.userValid(user);

        if (userO.isEmpty()) {
            user.setId(id++);
            users.put(user.getId(), user);
            log.info("Добавлен новый пользователь");
            return new ResponseEntity<>(user, HttpStatus.CREATED);
        } else {
            log.warn("Пользователь с таким логином и/или email уже есть");
            return new ResponseEntity<>(user, HttpStatus.OK);
        }

    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        FilmRateValidator.userValid(user);
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info("Данные пользователя обновлены");
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.warn("Пользователя с id=" + user.getId() + " не найден");
            return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteUser(Long id) {
    }

    @Override
    public User getUserById(Long id) {
        return users.get(id);
    }

    @Override
    public boolean removeFriend(Long id, Long friendId) {
        if (users.containsKey(id)) {
            users.get(id).removeFriend(friendId);
            return true;
        }
        return false;
    }
}
