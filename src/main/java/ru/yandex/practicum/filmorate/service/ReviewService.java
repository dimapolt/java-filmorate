package ru.yandex.practicum.filmorate.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);
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
        try {
            checkReviewExist(review);
            Review updatedReview = reviewStorage.updateReview(review);
            // добавляем обновление отзыва в ленте событий
            return updatedReview;
        } catch (ReviewNotFoundException exc) {
            log.info("Отзыв с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод updateReview", review.getReviewId());
            String message = String.format("Отзыв с id=%d не найден в базе данных! Невозможно выполнить операцию " +
                    "обновления", review.getReviewId());
            throw new ReviewNotFoundException(message);
        }
    }

    public Review getReviewById(long id) {
        try {
            return reviewStorage.getReviewById(id);
        } catch (ReviewNotFoundException exc) {
            log.info("Отзыв с id={} не найден в базе данных! Невозможно выполнить метод getReviewById", id);
            String message = String.format("Отзыв с id=%d не найден в базе данных!", id);
            throw new ReviewNotFoundException(message);
        }
    }

    public void deleteReviewById(long id) {
        try {
            Review deletedReview = reviewStorage.getReviewById(id); // получаем отзыв на фильм из БД перед удалением
            reviewStorage.deleteReviewById(id); // удаляем отзыв на фильм из БД
            // добавляем удаление отзыва в ленту событий
        } catch (ReviewNotFoundException exc) {
            log.info("Отзыв с id={} не найден в базе данных! Невозможно выполнить метод deleteReviewById", id);
            String message = String.format("Отзыв с id=%d не найден в базе данных! Невозможно выполнить операцию " +
                    "удаления", id);
            throw new ReviewNotFoundException(message);
        }
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
            log.info("Отзыв с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод addLikeOrDislikeForFilmReview", reviewId);
            String message = String.format("Отзыв с id=%d не найден в базе данных!", reviewId);
            throw new ReviewNotFoundException(message);
        } catch (NoDataFoundException exc) {
            log.info("Пользователь с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод addLikeOrDislikeForFilmReview", userId);
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
            log.info("Отзыв с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод deleteLikeOrDislikeForFilmReview", reviewId);
            String message = String.format("Отзыв с id=%d не найден в базе данных!", reviewId);
            throw new ReviewNotFoundException(message);
        } catch (NoDataFoundException exc) {
            log.info("Пользователь с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод deleteLikeOrDislikeForFilmReview", userId);
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
            log.info("Фильм с id={} не найден в базе данных! " +
                    "Невозможно выполнить метод getAllReviewsOrSomeCountReviewsByFilmId", filmId);
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

    private void checkReviewExist(Review review) {
        long reviewId = review.getReviewId();
        reviewStorage.getReviewById(reviewId); // метод выбрасывает ReviewNotFoundException
    }
}
