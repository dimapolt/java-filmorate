package ru.yandex.practicum.filmorate.storage.mpa;

import org.springframework.beans.factory.annotation.Autowired;
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
public class MpaDbStorage implements MpaStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Mpa> getAllMpa() {
        String sqlQuery = "SELECT * FROM rating;";
        List<Mpa> mpa = new ArrayList<>();

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sqlQuery);

        while (rowSet.next()) {
            mpa.add(new Mpa(rowSet.getInt("rating_id"), rowSet.getString("name")));
        }

        return mpa;
    }

    @Override
    public Mpa getMpaById(Integer id) {
        String sqlQuery = "SELECT * FROM rating WHERE rating_id = ?;";

        try {
            Mpa mpa = jdbcTemplate.queryForObject(sqlQuery, this::mapRowToMpa, id);
            return mpa;
        } catch (EmptyResultDataAccessException e) {
            throw new NoDataFoundException("Рейтинга с таким id ещё нет в базе");
        }
    }

    private Mpa mapRowToMpa(ResultSet resultSet, int rowNum) throws SQLException {
        return new Mpa(resultSet.getInt("rating_id"), resultSet.getString("name"));
    }
}
