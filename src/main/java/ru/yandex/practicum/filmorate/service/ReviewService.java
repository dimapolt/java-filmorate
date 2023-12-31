package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

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
        return reviewStorage.getReviewById(id); // метод выбрасывает NoDataException если пользователь не найден по id
    }

    public void deleteReviewById(long id) {
        Review deletedReview = reviewStorage.getReviewById(id); // получаем отзыв на фильм из БД перед удалением
        reviewStorage.deleteReviewById(id); // удаляем отзыв на фильм из БД
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
