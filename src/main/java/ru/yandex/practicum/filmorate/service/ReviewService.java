package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.review.ReviewStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;

import java.util.List;

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

        Review updatedReview = reviewStorage.updateReview(review);
        // добавляем обновление отзыва в ленте событий
        return updatedReview;
    }

    public Review getReviewById(long id) {
        return reviewStorage.getReviewById(id); // метод выбрасывает NoDataException если отзыв не найден по id
    }

    public void deleteReviewById(long id) {
        Review deletedReview = reviewStorage.getReviewById(id); // получаем отзыв на фильм из БД перед удалением
        reviewStorage.deleteReviewById(id); // удаляем отзыв на фильм из БД
        // добавляем удаление отзыва в ленту событий
    }

    public void addLikeOrDislikeForFilmReview(long reviewId, long userId, boolean isLike) {
        try {
            // проверяем на наличие отзыва на фильм в БД
            reviewStorage.getReviewById(reviewId);
            // проверяем на наличие пользователя в БД
            userStorage.getUserById(userId);

            if (isLike) {
                // ставим лайк отзыву, т.е добавляем запись в таблицу reviews_likes
                reviewStorage.addLikeOrDislikeForFilmReview(reviewId, userId, true);
                // увеличиваем рейтинг полезности отзыва, обновляя поле useful в таблице reviews
                reviewStorage.increaseUsefulForFilmReview(reviewId);
            } else {
                // ставим дизлайк отзыву, т.е добавляем запись в таблицу reviews_likes
                reviewStorage.addLikeOrDislikeForFilmReview(reviewId, userId, false);
                // уменьшаем рейтинг полезности отзыва, обновляя поле useful в таблице reviews
                reviewStorage.decreaseUsefulForFilmReview(reviewId);
            }
        } catch (ReviewNotFoundException exc) {
            String message = String.format("Отзыв с id=%d не найден в базе данных!", reviewId);
            throw new ReviewNotFoundException(message);
        } catch (NoDataFoundException exc) {
            String message = String.format("Пользователь с id=%d не найден в базе данных!", userId);
            throw new NoDataFoundException(message);
        }
    }

    public void deleteLikeOrDislikeForFilmReview(long reviewId, long userId) {
        try {
            // проверяем на наличие отзыва на фильм в БД преждем удалять лайк/дизлайк
            reviewStorage.getReviewById(reviewId);
            // проверяем на наличие пользователя в БД прежде чем удалять лайк/дизлайк
            userStorage.getUserById(userId);
            // удаляем лайк/дизлайк у отзыва
            reviewStorage.deleteLikeOrDislikeForFilmReview(reviewId, userId);
        } catch (ReviewNotFoundException exc) {
            String message = String.format("Отзыв с id=%d не найден в базе данных!", reviewId);
            throw new ReviewNotFoundException(message);
        } catch (NoDataFoundException exc) {
            String message = String.format("Пользователь с id=%d не найден в базе данных!", userId);
            throw new NoDataFoundException(message);
        }
    }

    public List<Review> getAllReviewsOrSomeCountReviewsByFilmId(Long filmId, int count) {
        try {
            if (filmId == null) {
                return reviewStorage.getAllReviews();
            }
            filmStorage.getFilmById(filmId); // Проверяем наличие фильма в БД
            return reviewStorage.getSomeCountReviewsByFilmId(filmId, count);
        } catch (NoDataFoundException exc) {
            String message = String.format("Фильм с id=%d не найден в базе данных!", filmId);
            throw new NoDataFoundException(message);
        }
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
