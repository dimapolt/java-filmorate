package ru.yandex.practicum.filmorate.storage.film;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getAllFilms();

    List<Film> getFilmsByDirector(Long id);

    List<Film> getFilmsByUser(Long id);

    ResponseEntity<Film> createFilm(Film film);

    ResponseEntity<Film> updateFilm(Film film);

    void deleteFilm(Long id);

    Film getFilmById(Long id);

    List<Film> getCommonFilms(Long userId, Long friendId);
}
