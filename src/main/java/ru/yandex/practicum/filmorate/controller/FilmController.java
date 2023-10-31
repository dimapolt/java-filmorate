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

        if (isValid(film)) {
            if (filmO.isEmpty()) {
                film.setId(id++);
                films.put(film.getId(), film);
                log.info("Добавлен новый фильм");
                return new ResponseEntity<>(film, HttpStatus.CREATED);
            } else {
                log.warn("Фильм с таким названием уже есть");
                return new ResponseEntity<>(film, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/films")
    public ResponseEntity<Film> updateFilm(@NonNull @RequestBody Film film) {
        if (isValid(film)) {
            if (films.containsKey(film.getId())) {
                films.put(film.getId(), film);
                log.info("Фильм обновлён");
                return new ResponseEntity<>(films.get(film.getId()), HttpStatus.OK);
            } else {
                log.warn("Фильма с id=" + film.getId() + " нет");
                return new ResponseEntity<>(film,HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(film, HttpStatus.BAD_REQUEST);
        }
    }

    private boolean isValid(@NonNull Film film) {
        if (Objects.equals(film.getName(), null) || film.getName().isBlank()) {
            log.warn("Передан фильм с пустым названием", new ValidationException("Название не может быть пустым!"));
            return false;
        } else if (film.getDescription().length() > 200) {
            log.warn("Передан фильм с описанием, длиной более 200 символов",
                    new ValidationException("Максимальная длина описания — 200 символов!"));
            return false;
        } else if (film.getReleaseDate().isBefore(FIRST_FILM)) {
            log.warn("Передан фильм с датой релиза ранее 28 декабря 1895 года",
                    new ValidationException("Дата релиза — не может быть раньше 28 декабря 1895 года!"));
            return false;
        } else if (film.getDuration() < 0) {
            log.warn("Передан фильм с отрицательной продолжительностью",
                    new ValidationException("Продолжительность фильма должна быть положительной!"));
            return false;
        }
        return true;
    }
}
