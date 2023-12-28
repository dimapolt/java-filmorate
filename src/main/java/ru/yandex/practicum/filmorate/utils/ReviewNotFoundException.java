package ru.yandex.practicum.filmorate.utils;

public class ReviewNotFoundException extends RuntimeException {

    public ReviewNotFoundException(final String message) {
        super(message);
    }
}
