package ru.yandex.practicum.filmorate.storage.director;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Director;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class DirectorDbStorage implements DirectorStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Director> getAllDirectors() {
        String sqlQuery = "SELECT * FROM directors;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToDirector);
    }

    @Override
    public Director getDirectorById(Long id) {
        String sqlQuery = "SELECT * FROM directors WHERE director_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToDirector, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Режиссёра с id=" + id + " нет в базе");
            throw new NoDataFoundException("Режиссёра с id=" + id + " нет в базе");
        }
    }

    @Override
    public ResponseEntity<Director> createDirector(Director director) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("directors")
                .usingGeneratedKeyColumns("director_id");

        long idReturn = simpleJdbcInsert.executeAndReturnKey(getMapFromDirector(director)).intValue();

        director.setId(idReturn);

        return new ResponseEntity<>(director, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Director> updateDirector(Director director) {
        String sqlQuery = "UPDATE directors SET " +
                "name = ? " +
                "WHERE director_id = ?";

        int result = jdbcTemplate.update(sqlQuery,
                director.getName(),
                director.getId());

        if (result == 1) {
            director = getDirectorById(director.getId());
            return new ResponseEntity<>(director, HttpStatus.OK);
        } else {
            log.warn("Режиссёр с id=" + director.getId() + " не обновлён, нет в базе");
            throw new NoDataFoundException("Режиссёр с id=" + director.getId() + " нет в базе");
        }
    }

    @Override
    public void deleteDirector(Long id) {
        String sqlQuery = "DELETE FROM directors WHERE director_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    private Director mapRowToDirector(ResultSet resultSet, int rowNum) throws SQLException {
        return new Director(resultSet.getLong("director_id"),
                resultSet.getString("name"));
    }

    private Map<String, Object> getMapFromDirector(Director director) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", director.getName());

        return values;
    }
}
