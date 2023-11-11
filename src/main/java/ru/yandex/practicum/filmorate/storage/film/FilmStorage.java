package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();
    ResponseEntity<Film> createFilm(Film film);
    ResponseEntity<Film> updateFilm(Film film);
    Film getFilmById(Long id);
}
