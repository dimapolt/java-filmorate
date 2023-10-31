package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    @Test
    void shouldReturn400WhenEmptyFilmCreate() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setDescription("Description without name");

        assertEquals(HttpStatus.BAD_REQUEST, filmController.createFilm(film).getStatusCode());
    }

    @Test
    void shouldReturn400WhenOver200SymbolsInDescription() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Cinema is an integral part of the social life of people. The movie combines not " +
                "only stunts, special effects and scenery, but also the genuine art. Many people go to the cinemas " +
                "in their free time. Over recent years");

        assertEquals(HttpStatus.BAD_REQUEST, filmController.createFilm(film).getStatusCode());
    }

    @Test
    void shouldReturn400WhenDateEarly1895() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Description without name");
        film.setReleaseDate(LocalDate.of(1894, 12, 31));

        assertEquals(HttpStatus.BAD_REQUEST, filmController.createFilm(film).getStatusCode());
    }

    @Test
    void shouldReturn400WhenDurationIsNegative() {
        FilmController filmController = new FilmController();
        Film film = new Film();
        film.setName("Cinema");
        film.setDescription("Description without name");
        film.setReleaseDate(LocalDate.now());
        film.setDuration(-1);

        assertEquals(HttpStatus.BAD_REQUEST, filmController.createFilm(film).getStatusCode());
    }


}