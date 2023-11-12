package ru.yandex.practicum.filmorate.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс содержащий сообщение о причинах исключения ValidationException и код ошибки, для ответа "фронтенду"
 */
@AllArgsConstructor
@Getter
public class ErrorDescription {
    private int httpStatusCode;
    private String message;
}
