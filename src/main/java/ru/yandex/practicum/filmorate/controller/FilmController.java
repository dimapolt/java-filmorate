package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.*;

@RestController
@Slf4j
public class FilmController {
    private static final LocalDate FIRST_FILM = LocalDate.of(1895, 12, 28);
    private Integer id = 1;
    private final Map<Integer, Film> films = new HashMap<>();

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен запрос на список всех фильмов");
        return new ArrayList<>(films.values());
    }

    @PostMapping("/films")
    public ResponseEntity<Film> createFilm(@NonNull @RequestBody Film film) {
        Optional<Film> filmO = films.values().stream()
                .filter(f -> f.getName().equals(film.getName()))
                .findFirst();

        checkValid(film);

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

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@NonNull @RequestBody Film film) {
            checkValid(film);

            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм обновлён");
                return new ResponseEntity<>(films.get(film.getId()), HttpStatus.OK);
            } else {
                log.warn("Фильма с id=" + film.getId() + " нет");
                return new ResponseEntity<>(film,HttpStatus.INTERNAL_SERVER_ERROR);
            }
    }

    private void checkValid(@NonNull Film film) {
        if (Objects.equals(film.getName(), null) || film.getName().isBlank()) {
            throw new ValidationException("Передан фильм с пустым названием");
        } else if (film.getDescription().length() > 200) {
            throw new ValidationException("Передан фильм с описанием, длиной более 200 символов");
        } else if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            throw new ValidationException("Передан фильм с датой релиза ранее 28 декабря 1895 года");
        } else if (film.getDuration() < 0) {
            throw new ValidationException("Передан фильм с отрицательной продолжительностью");
        }
    }
}
