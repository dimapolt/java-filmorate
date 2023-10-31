package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    @Test
    void shouldReturn400WhenWrongEmail() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin("user1");
        user.setEmail("user");

        assertEquals(HttpStatus.BAD_REQUEST, userController.createUser(user).getStatusCode());
    }

    @Test
    void shouldReturn400WhenEmptyLogin() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin(" ");
        user.setEmail("user@ya.ru");

        assertEquals(HttpStatus.BAD_REQUEST, userController.createUser(user).getStatusCode());
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
    void shouldReturn400WhenBirthdayInFuture() {
        UserController userController = new UserController();
        User user = new User();
        user.setLogin("user");
        user.setEmail("user@ya.ru");
        user.setBirthday(LocalDate.of(2050,1,1));

        assertEquals(HttpStatus.BAD_REQUEST, userController.createUser(user).getStatusCode());
    }
}