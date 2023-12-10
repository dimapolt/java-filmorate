package ru.yandex.practicum.filmorate.controller;

import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.getAllFilms();
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Long filmId) {
        return filmService.getFilmById(filmId);
    }

    @PostMapping
    public ResponseEntity<Film> createFilm(@NonNull @RequestBody Film film) {
        return filmService.createFilm(film);
    }

    @PutMapping
    public ResponseEntity<Film> updateFilm(@NonNull @RequestBody Film film) {
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public String setLike(@NonNull @PathVariable("id") Long filmId, @NonNull @PathVariable Long userId) {
        return filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String unSetLike(@NonNull @PathVariable("id") Long filmId, @NonNull @PathVariable Long userId) {
        return filmService.setLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsByLikes(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getFilmsByLikes(count);
    }


}
