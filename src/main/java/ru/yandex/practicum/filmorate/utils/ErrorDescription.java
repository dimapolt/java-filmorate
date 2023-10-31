package ru.yandex.practicum.filmorate.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
//Класс содержащий сообщение о причинах исключения ValidationException и код ошибки, для ответа "фронтенду"
public class ErrorDescription {
    private int httpStatusCode;
    private String message;
}
