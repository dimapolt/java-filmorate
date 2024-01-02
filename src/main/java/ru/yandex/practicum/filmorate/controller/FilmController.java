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

    @DeleteMapping("/{filmId}")
    public String deleteFilm(@PathVariable Long filmId) {
        return filmService.deleteFilm(filmId);
    }

    @PutMapping("/{id}/like/{userId}")
    public String setLike(@NonNull @PathVariable("id") Long filmId, @NonNull @PathVariable Long userId) {
        return filmService.setLike(filmId, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public String unSetLike(@NonNull @PathVariable("id") Long filmId, @NonNull @PathVariable Long userId) {
        return filmService.unSetLike(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getFilmsByLikes(@RequestParam(defaultValue = "10") Integer count) {
        return filmService.getFilmsByLikes(count);
    }

    @GetMapping("/director/{directorId}")
    public List<Film> getFilmsByDirector(@PathVariable("directorId") Long id, @RequestParam String sortBy) {
        return filmService.getFilmsByDirector(id, sortBy);
    }

    @GetMapping("/common")
    public List<Film> getCommonFilms(@RequestParam(required = true) Long userId,
                                     @RequestParam(required = true) Long friendId) {
        return filmService.getCommonFilms(userId, friendId);
    }

    @GetMapping("/search")
    public List<Film> getFilmsByParameters(@RequestParam(required = false) String query,
                                           @RequestParam(required = false) String by) {
        return filmService.getFilmsByParameters(query, by);
    }

}
