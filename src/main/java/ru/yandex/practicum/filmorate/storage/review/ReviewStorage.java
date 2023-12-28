package ru.yandex.practicum.filmorate.storage.review;

import ru.yandex.practicum.filmorate.model.Review;

import java.util.List;

public interface ReviewStorage {
    Review createReview(Review review);

    Review updateReview(Review review);

    List<Review> getAllReviews();

    Review getReviewById(int id);

    void deleteReviewById(int id);

    void addLikeForFilmReview(int reviewId, int userId);

    void addDislikeForFilmReview(int reviewId, int userId);

    void deleteLikeForFilmReview(int reviewId, int userId);

    void deleteDislikeForFilmReview(int reviewId, int userId);
}
