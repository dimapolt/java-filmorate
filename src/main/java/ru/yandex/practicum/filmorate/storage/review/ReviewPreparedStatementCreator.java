package ru.yandex.practicum.filmorate.storage.review;

import org.springframework.jdbc.core.PreparedStatementCreator;
import ru.yandex.practicum.filmorate.model.Review;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class ReviewPreparedStatementCreator implements PreparedStatementCreator {
    private final String sqlQuery;
    private final Review review;

    public ReviewPreparedStatementCreator(String sqlQueryArg, Review reviewArg) {
        sqlQuery = sqlQueryArg;
        review = reviewArg;
    }

    @Override
    public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        String content = review.getContent();
        boolean isPositive = review.isPositive();
        long filmId = review.getFilmId();
        long userId = review.getUserId();

        PreparedStatement ps = con.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
        ps.setString(1, content);
        ps.setBoolean(2, isPositive);
        ps.setLong(3, filmId);
        ps.setLong(4, userId);
        return ps;
    }
}
