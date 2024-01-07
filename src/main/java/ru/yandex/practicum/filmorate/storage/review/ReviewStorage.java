package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review createReview(Review review);

    Review updateReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(long id);

    void deleteReviewById(long id);

    void addLikeOrDislikeForFilmReview(long reviewId, long userId, boolean isLike);

    void deleteLikeOrDislikeForFilmReview(long reviewId, long userId);

    void increaseUsefulForFilmReview(long reviewId);

    void decreaseUsefulForFilmReview(long reviewId);
}
