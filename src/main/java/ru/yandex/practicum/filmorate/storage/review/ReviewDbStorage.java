package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Review;

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
        return null;
    }

    @Override
    public List<Review> getAllReviews() {
        return null;
    }

    @Override
    public Review getReviewById(int id) {
        return null;
    }

    @Override
    public void deleteReviewById(int id) {

    }

    @Override
    public void addLikeForFilmReview(int reviewId, int userId) {

    }

    @Override
    public void addDislikeForFilmReview(int reviewId, int userId) {

    }

    @Override
    public void deleteLikeForFilmReview(int reviewId, int userId) {

    }

    @Override
    public void deleteDislikeForFilmReview(int reviewId, int userId) {

    }
}
