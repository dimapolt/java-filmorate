package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

@RestController @Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Получен запрос на список всех пользователей");
        return new ArrayList<>(users.values());
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@NonNull @RequestBody User user) {

        Optional<User> userO = users.values()
                .stream()
                .filter(u -> u.getLogin().equals(user.getLogin()) || u.getEmail().equals(user.getEmail()))
                .findFirst();

            if (isValid(user)) {
                if (userO.isEmpty()) {
                    user.setId(id++);
                    users.put(user.getId(), user);
                    log.info("Добавлен новый пользователь");
                    return new ResponseEntity<>(user, HttpStatus.CREATED);
                }  else {
                    log.warn("Пользователь с таким логином и/или email уже есть");
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            } else {
                return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
            }

        }

    @PutMapping("/users")
    public ResponseEntity<User> updateFilm(@NonNull @RequestBody User user) {
        if (isValid(user)) {
            if (users.containsKey(user.getId())) {
                users.put(user.getId(), user);
                log.info("Данные пользователя обновлены");
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                log.warn("Пользователя с id=" + user.getId() + " не найден");
                return new ResponseEntity<>(user, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(user, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValid(@NonNull User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            log.warn("Передан некорректный email адрес", new ValidationException("Передан некорректный email адрес"));
            return false;
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            log.warn("Некорректный логин", new ValidationException("Логин не может быть пустым и содержать пробелы"));
            return false;
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("Дата рождения из будущего", new ValidationException("Дата рождения не может быть в будущем"));
            return false;
        }

        if (Objects.equals(user.getName(), null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Именем пользователя установлен логин, т.к. имя передано пустым");
        }
        return true;
    }
}
