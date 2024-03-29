package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmExtraValidator;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.utils.QueriesProvider.getQuery;

@Service
@Slf4j
public class FilmService {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final FilmExtraValidator filmExtraValidator;

    private final EventStorage eventStorage;

    @Autowired
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage,
                       @Qualifier("userDbStorage") UserStorage userStorage,
                       FilmExtraValidator filmExtraValidator, @Qualifier("eventDbStorage") EventStorage eventStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.eventStorage = eventStorage;
        this.filmExtraValidator = filmExtraValidator;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(Long filmId) {
        return filmStorage.getFilmById(filmId);
    }

    /**
     * Перед созданием или обновлением делаем проверку на наличие
     * характеристик (жанры, рейтинг, режисёры) в базе данных через
     * объект класса <b>FilmExtraValidator<b/>
     */
    public ResponseEntity<Film> createFilm(Film film) {
        filmExtraValidator.checkFilmCharacteristic(film);
        return filmStorage.createFilm(film);
    }

    public ResponseEntity<Film> updateFilm(Film film) {
        filmExtraValidator.checkFilmCharacteristic(film);
        return filmStorage.updateFilm(film);
    }

    public String deleteFilm(Long filmId) {
        filmStorage.deleteFilm(filmId);

        return "Фильм с id=" + filmId + " успешно удален!";
    }

    public String setLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        FilmRateValidator.checkOnNull(film, user);

        film.setLike(userId);

        filmStorage.updateFilm(film);
        eventStorage.createEvent(Event.EntityType.LIKE, filmId,
                Event.EventOperationType.ADD, userId);

        return "Пользователь " + userId + " поставил оценку фильму " + filmId;
    }

    public String unSetLike(Long filmId, Long userId) {
        Film film = filmStorage.getFilmById(filmId);
        User user = userStorage.getUserById(userId);
        FilmRateValidator.checkOnNull(film, user);

        film.unSetLike(userId);

        filmStorage.updateFilm(film);
        eventStorage.createEvent(Event.EntityType.LIKE, filmId,
                Event.EventOperationType.REMOVE, userId);

        return "Удалена оценка от " + userId + " фильму " + filmId;
    }

    public List<Film> getFilmsByLikes(Integer count, Integer genreId, Integer year) {
        List<Film> films = filmStorage.getAllFilms();

        films.sort((f1, f2) -> f2.getLikesCount() - f1.getLikesCount());

        if (genreId != null)
            films = films.stream()
                    .filter(film -> film.getGenres().stream()
                            .anyMatch(genre -> Objects.equals(genre.getId(), genreId)))
                    .collect(Collectors.toList());

        if (year != null)
            films = films.stream()
                    .filter(film -> film.getReleaseDate().getYear() == year)
                    .collect(Collectors.toList());

        if (films.size() > count && count != 0) {
            return films.subList(0, count);
        } else {
            return films;
        }
    }

    public List<Film> getFilmsByDirector(Long id, String sortBy) {
        if (!(sortBy.equals("year") || sortBy.equals("likes"))) {
            throw new ValidationException("Неверное условие для сортировки!");
        }

        List<Film> films = filmStorage.getFilmsByDirector(id);

        if (films.size() == 0) {
            String message = "У данного режиссёра отсутствуют фильмы либо режиссёра нет в базе";
            log.warn(message);
            throw new NoDataFoundException(message);
        }

        if (sortBy.equals("year")) {
            films.sort((f1, f2) -> {
                if (f1.getReleaseDate().isAfter(f2.getReleaseDate())) {
                    return 1;
                } else if ((f1.getReleaseDate().isBefore(f2.getReleaseDate()))) {
                    return -1;
                } else {
                    return 0;
                }
            });
        } else { // если 'likes'
            films.sort(Comparator.comparingInt(Film::getLikesCount).reversed());
        }

        return films;
    }

    public List<Film> getCommonFilms(Long userId, Long friendId) {
        List<Film> films = filmStorage.getCommonFilms(userId, friendId);

        films.sort((f1, f2) -> f2.getLikesCount() - f1.getLikesCount());

        return films;
    }

    public List<Film> getFilmsByParameters(String query, String by) {
        if (query == null || by == null) {
            return getFilmsByLikes(0, null, null);
        } else {
            String sqlQuery = getQuery(query.toLowerCase(), by);
            List<Film> films = filmStorage.getFilmsByParameters(sqlQuery);
            films.sort(Comparator.comparing(Film::getId).reversed());
            return films;
        }
    }

}
