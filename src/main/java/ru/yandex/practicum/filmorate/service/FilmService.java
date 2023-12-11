package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.util.List;

@Service
@Slf4j
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;


    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        Film film = filmStorage.getFilmById(filmId);
        FilmRateValidator.checkOnNull(film, "Фильм с id=" + filmId + " не найден!");

        return film;
    }

    public ResponseEntity<Film> createFilm(Film film) {
        return filmStorage.createFilm(film);
    }

    public ResponseEntity<Film> updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public String setLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        FilmRateValidator.checkOnNull(film, user);

        film.setLike(userId);

        filmStorage.updateFilm(film);

        return "Пользователь " + userId + " поставил оценку фильму " + filmId;
    }

    public String unSetLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        FilmRateValidator.checkOnNull(film, user);

        film.unSetLike(userId);

        return "Удалена оценка от " + userId + " фильму " + filmId;
    }

    public List<Film> getFilmsByLikes(Integer count) {
        List<Film> films = filmStorage.getAllFilms();

        films.sort((f1, f2) -> f2.getLikesCount() - f1.getLikesCount());

        if (films.size() >= count) {
            return films.subList(0, count);
        } else {
            return films;
        }
    }

}
