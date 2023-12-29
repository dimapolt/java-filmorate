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
import ru.yandex.practicum.filmorate.utils.ReviewNotFoundException;

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
        checkFilmExist(review);
        checkUserExist(review);
        Review newReview = reviewStorage.createReview(review); // создаем новый отзыв на фильм
        // добавляем новый отзыв в ленту событий
        return newReview;
    }

    public Review updateReview(Review review) {
        checkFilmExist(review);
        checkUserExist(review);
        Review updatedReview = reviewStorage.updateReview(review);
        // добавляем обновление отзыва в ленте событий
        return updatedReview;
    }

    public Review getReviewById(long id) {
        Review review = reviewStorage.getReviewById(id);
        if (review == null) {
            String message = String.format("Отзыв с id=%d не найден в базе данных!", id);
            throw new ReviewNotFoundException(message);
        }
        return review;
    }

    public void deleteReviewById(long id) {
        Long reviewId = reviewStorage.deleteReviewById(id);
        if (reviewId == null) {
            String message = String.format("Отзыв с id=%d не найден в базе данных! Операция удаления невозможна!", id);
            throw new ReviewNotFoundException(message);
        }
        // добавляем удаление отзыва в ленту событий
    }

    private void checkFilmExist(Review review) {
        long filmId = review.getFilmId();
        filmStorage.getFilmById(filmId); // метод выбрасывает NoDataException если фильм не найден по id
    }

    private void checkUserExist(Review review) {
        long userId = review.getUserId();
        userStorage.getUserById(userId); // метод выбрасывает NoDataException если пользователь не найден по id
    }
}
