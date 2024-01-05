package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmExtraValidator;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class FilmControllerTest {
    FilmController filmController;
    @InjectMocks
    FilmService filmService;
    FilmStorage filmStorage;
    UserStorage userStorage;
    EventStorage eventStorage;
    @Mock
    FilmExtraValidator filmExtraValidator;

    @BeforeEach
    void init() {
        filmStorage = new InMemoryFilmStorage();
        filmService = new FilmService(filmStorage, userStorage, filmExtraValidator, eventStorage);
        filmController = new FilmController(filmService);
    }

    @Test
    void shouldThrowExceptionWhenEmptyFilmCreate() {
        Film film = new Film();
        film.setDescription("Description without name");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));
        assertEquals("Передан фильм с пустым названием", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenOver200SymbolsInDescription() {
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Cinema is an integral part of the social life of people. The movie combines not " +
                "only stunts, special effects and scenery, but also the genuine art. Many people go to the cinemas " +
                "in their free time. Over recent years");

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));
        assertEquals("Передан фильм с описанием, длиной более 200 символов", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDateEarly1895() {
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Description without name");
        film.setReleaseDate(LocalDate.of(1894, 12, 31));

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));
        assertEquals("Передан фильм с датой релиза ранее 28 декабря 1895 года", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenDurationIsNegative() {
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Description without name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-1);

        final ValidationException exception = assertThrows(ValidationException.class,
                () -> filmController.createFilm(film));
        assertEquals("Передан фильм с отрицательной продолжительностью", exception.getMessage());
    }

}