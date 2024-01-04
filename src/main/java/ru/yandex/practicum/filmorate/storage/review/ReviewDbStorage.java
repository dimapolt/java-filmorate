package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.exceptions.ReviewNotFoundException;

import java.util.List;
import java.util.Objects;

@Component
public class ReviewDbStorage implements ReviewStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ReviewDbStorage(JdbcTemplate jdbcTemplateArg) {
        jdbcTemplate = jdbcTemplateArg;
    }

    @Override
    public Review createReview(Review review) {
        String sqlQuery = "INSERT INTO reviews (content, isPositive, film_id, user_id) " +
                "VALUES (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new ReviewPreparedStatementCreator(sqlQuery, review), keyHolder);
        long id = Objects.requireNonNull(keyHolder.getKey()).longValue();
        review.setReviewId(id);
        return review;
    }

    @Override
    public Review updateReview(Review review) {
        String sqlQuery = "UPDATE reviews SET " +
                "content = ?, " +
                "isPositive = ? " +
                "film_id = ? " +
                "user_id = ? " +
                "WHERE review_id = ?";
        String content = review.getContent();
        boolean isPositive = review.getIsPositive();
        long filmId = review.getFilmId();
        long userId = review.getUserId();
        long reviewId = review.getReviewId();
        Object[] args = {content, isPositive, filmId, userId, reviewId};

        int countUpdatedRows = jdbcTemplate.update(sqlQuery, args);
        if (countUpdatedRows == 0) {
            String message = String.format("Отзыв с id=%d не найден в базе данных! Операция обновления невозможна!",
                    review.getReviewId());
            throw new ReviewNotFoundException(message);
        }
        return review;
    }

    @Override
    public List<Review> getAllReviews() {
        String sqlQuery = "SELECT * FROM reviews";
        return jdbcTemplate.query(sqlQuery, new ReviewResultSetExtractor());
    }

    @Override
    public Review getReviewById(long id) {
        String sqlQuery = "SELECT * FROM reviews WHERE review_id = ?";
        List<Review> reviews = jdbcTemplate.query(sqlQuery, new ReviewResultSetExtractor(), id);
        assert reviews != null;
        if (reviews.isEmpty()) {
            String message = String.format("Отзыв с id=%d не найден в базе данных!", id);
            throw new ReviewNotFoundException(message);
        }
        return reviews.get(0);
    }

    @Override
    public void deleteReviewById(long id) {
        String sqlQuery = "DELETE FROM reviews WHERE review_id = ?";
        int result = jdbcTemplate.update(sqlQuery, id);
        if (result == 0) {
            String message = String.format("Отзыв с id=%d не найден в базе данных! Операция удаления невозможна!", id);
            throw new ReviewNotFoundException(message);
        }
    }

    @Override
    public void addLikeOrDislikeForFilmReview(long reviewId, long userId, boolean isLike) {
        String sqlQuery = "INSERT INTO reviews_likes (review_id, user_id, isLike) " +
                "VALUES (?, ?, ?)";
        jdbcTemplate.update(sqlQuery, reviewId, userId, isLike);
    }

    @Override
    public void deleteLikeOrDislikeForFilmReview(long reviewId, long userId) {
        String sqlQuery = "DELETE FROM reviews_likes " +
                "WHERE review_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, reviewId, userId);
    }
}
