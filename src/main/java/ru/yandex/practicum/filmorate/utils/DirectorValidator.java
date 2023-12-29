package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Director;

@Slf4j
public class DirectorValidator {

    public static void checkDirectorValid(Director director) {
        if (director.getName().isBlank() || director.getName().isEmpty()) {
            log.warn("Отсутствует имя режиссёра");
            throw new ValidationException("Отсутствует имя режиссёра");
        }
    }
}
