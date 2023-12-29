package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review createReview(Review review);

    Review updateReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(long id);

    Long deleteReviewById(long id);

    void addLikeForFilmReview(long reviewId, long userId);

    void addDislikeForFilmReview(long reviewId, long userId);

    void deleteLikeForFilmReview(long reviewId, long userId);

    void deleteDislikeForFilmReview(long reviewId, long userId);
}
