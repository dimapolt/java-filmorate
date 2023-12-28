package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;


@Service
public class ReviewService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private final ReviewStorage reviewStorage;

    @Autowired
    public ReviewService(@Qualifier("filmDbStorage") FilmStorage filmStorageArg,
                         @Qualifier("userDbStorage") UserStorage userStorageArg,
                         ReviewStorage reviewStorageArg) {
        filmStorage = filmStorageArg;
        userStorage = userStorageArg;
        reviewStorage = reviewStorageArg;
    }

    public Review createReview(Review review) {
        long filmId = review.getFilmId();
        Film film = filmStorage.getFilmById(filmId);
        String filmMessage = String.format("Фильм с id=%d не найден в базе данных!", filmId);
        FilmRateValidator.checkOnNull(film, filmMessage);

        long userId = review.getUserId();
        User user = userStorage.getUserById(userId);
        String userMessage = String.format("Пользователь с id=%d не найден в базе данных!", userId);
        FilmRateValidator.checkOnNull(user, userMessage);

        return reviewStorage.createReview(review); // создаем новый отзыв на фильм
    }
}
