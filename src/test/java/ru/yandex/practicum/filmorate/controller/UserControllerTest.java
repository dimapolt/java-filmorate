package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void shouldThrowExceptionWhenWrongEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin("user1");
        user.setEmail("user");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(user));
        assertEquals("Передан некорректный email адрес", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenEmptyLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin(" ");
        user.setEmail("user@ya.ru");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(user));
        assertEquals("Пустой логин или содержит пробелы", exception.getMessage());
    }

    @Test
    void shouldReturnLoginAsNameWhenEmptyName() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin("user");
        user.setEmail("user@ya.ru");
        user.setBirthday(LocalDate.now());

        assertEquals("user", userController.createUser(user).getBody().getName());
    }

    @Test
    void shouldThrowExceptionWhenBirthdayInFuture() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin("user");
        user.setEmail("user@ya.ru");
        user.setBirthday(LocalDate.of(2050,1,1));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> userController.createUser(user));
        assertEquals("Дата рождения из будущего", exception.getMessage());

    }
}