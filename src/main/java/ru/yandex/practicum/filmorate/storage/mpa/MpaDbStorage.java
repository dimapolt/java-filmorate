package ru.yandex.practicum.filmorate.storage.mpa;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM ratings;";
        List<Mpa> mpa = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);

        while (rowSet.next()) {
            mpa.add(new Mpa(rowSet.getInt("rating_id"), rowSet.getString("name")));
        }

        return mpa;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM ratings WHERE rating_id = ?;";

        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
        } catch (EmptyResultDataAccessException e) {
            throw new NoDataFoundException("Рейтинга с таким id ещё нет в базе");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"), resultSet.getString("name"));
    }
}
