package ru.yandex.practicum.filmorate.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.DirectorService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

/**
 * Класс проверки характеристик пришедшего
 * на создание/обновление фильма.
 * Проверка характеристик (Рейтинга, Жанров, Режиссёров)
 * на наличие в базе данных
 */

@Component
@RequiredArgsConstructor
public class FilmExtraValidator {
    private final MpaService mpaService;
    private final GenreService genreService;
    private final DirectorService directorService;


    public void checkFilmCharacteristic(Film film) {
        checkMpaExist(film.getMpa());
        checkGenreExist(film.getGenres());
        checkDirectorExist(film.getDirectors());
    }

    // Наличие рейтинга
    private void checkMpaExist(Mpa mpa) {
        if (mpa.getId() == 0) {
            return;
        }
        mpaService.getMpaById(mpa.getId());
    }

    // Проверка списка всех жанров
    private void checkGenreExist(List<Genre> genres) {
        if (genres.size() == 0) {
            return;
        }

        for (Genre genre : genres) {
            genreService.getGenreById(genre.getId());
        }
    }

    // Проверка списка всех режиссёров
    private void checkDirectorExist(List<Director> directors) {
        if (directors.size() == 0) {
            return;
        }

        for (Director director : directors) {
            directorService.getDirectorById(director.getId());
        }
    }
}