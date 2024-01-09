package ru.yandex.practicum.filmorate.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Objects;

@Slf4j
public class FilmRateValidator {
    private static final LocalDate FIRST_FILM_DATE = LocalDate.of(1895, 12, 28);

    public static void checkOnNull(Film film, User user) {
        if (film == null) {
            throw new NoDataFoundException("Фильм не найден!");
        }
        if (user == null) {
            throw new NoDataFoundException("Пользователь не найден!");
        }
    }

    public static void checkOnNull(Film film, String message) {
        if (film != null) {
            log.info("Получен запрос на получения фильма");
        } else {
            log.warn(message);
            throw new NoDataFoundException(message);
        }
    }

    public static void checkOnNull(User user, String message) {
        if (user != null) {
            log.info("Получен запрос на получение пользователя");
        } else {
            log.warn(message);
            throw new NoDataFoundException(message);
        }
    }

    public static void filmValid(@NonNull Film film) {
        if (Objects.equals(film.getName(), null) || film.getName().isBlank()) {
            throw new ValidationException("Передан фильм с пустым названием");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Передан фильм с описанием, длиной более 200 символов");
        } else if (film.getReleaseDate().isBefore(FIRST_FILM_DATE)) {
            throw new ValidationException("Передан фильм с датой релиза ранее 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Передан фильм с отрицательной продолжительностью");
        }
    }

    public static void userValid(@NonNull User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            throw new ValidationException("Передан некорректный email адрес");
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            throw new ValidationException("Пустой логин или содержит пробелы");
        } else if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения из будущего");
        }

        if (Objects.equals(user.getName(), null) || user.getName().isBlank()) {
            user.setName(user.getLogin());
            log.info("Именем пользователя установлен логин, т.к. имя передано пустым");
        }

    }

}
