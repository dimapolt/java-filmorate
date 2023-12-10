package ru.yandex.practicum.filmorate.exceptions;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(String message) {
        super(message);
    }
}
