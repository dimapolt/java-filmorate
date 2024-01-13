package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.util.*;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {
    private Long id = 1L;
    private final Map<Long, Film> films = new HashMap<>();

    @Override
    public List<Film> getAllFilms() {
        log.info("Получен запрос на список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @Override
    public List<Film> getFilmsByDirector(Long id) {
        return new ArrayList<>();
    }

    @Override
    public Film getFilmById(Long id) {
        return films.get(id);
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        Optional<Film> filmO = films.values().stream()
                .filter(f -> f.getName().equals(film.getName()))
                .findFirst();

        FilmRateValidator.filmValid(film);

        if (filmO.isEmpty()) {
            film.setId(id++);
            films.put(film.getId(), film);
            log.info("Добавлен новый фильм");
            return new ResponseEntity<>(film, HttpStatus.CREATED);
        } else {
            log.warn("Фильм с таким названием уже есть");
            return new ResponseEntity<>(film, HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        FilmRateValidator.filmValid(film);

        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info("Фильм обновлён");
            return new ResponseEntity<>(films.get(film.getId()), HttpStatus.OK);
        } else {
            log.warn("Фильма с id=" + film.getId() + " нет");
            return new ResponseEntity<>(film, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteFilm(Long id) {
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        return new ArrayList<>();
    }

    @Override
    public List<Film> getFilmsByParameters(String sqlQuery) {
        return new ArrayList<>();
    }
}
