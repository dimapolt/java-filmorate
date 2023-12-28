package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReviewResultSetExtractor implements ResultSetExtractor<List<Review>> {
    @Override
    public List<Review> extractData(ResultSet rs) throws SQLException, DataAccessException {
        List<Review> reviewList = new ArrayList<>();
        while (rs.next()) {
            long id = rs.getLong("review_id");
            String content = rs.getString("content");
            boolean isPositive = rs.getBoolean("isPositive");
            long filmId = rs.getLong("film_id");
            long userId = rs.getLong("user_id");
            int useful = rs.getInt("useful");

            Review review = new Review(id, content, isPositive, filmId, userId, useful);
            reviewList.add(review);
        }
        return reviewList;
    }
}
