package ru.yandex.practicum.filmorate.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

/**
 * Обработчик исключений
 */
@ControllerAdvice
@Slf4j
public class FilmRateExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<ErrorDescription> catchValidationException(ValidationException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(new ErrorDescription(HttpStatus.BAD_REQUEST.value(),
                exception.getMessage()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler ({NoDataFoundException.class})
    public ResponseEntity<ErrorDescription> catchNotFoundException(RuntimeException exception) {
        log.warn(exception.getMessage());
        return new ResponseEntity<>(new ErrorDescription(HttpStatus.NOT_FOUND.value(),
                exception.getMessage()),
                HttpStatus.NOT_FOUND);
    }


}
