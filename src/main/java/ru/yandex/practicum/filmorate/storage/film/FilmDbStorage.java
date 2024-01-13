package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        String sqlQuery = "SELECT * FROM films;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public List<Film> getFilmsByDirector(Long id) {
        String sqlQuery = "SELECT fd.film_id FROM film_director fd WHERE director_id = ?;";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> getFilmById(rs.getLong("film_id")),
                id);

    }

    public List<Film> getLikedFilms(Long id) {
        String sqlQuery = "SELECT l.film_id FROM likes l WHERE user_id = ?;";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> getFilmById(rs.getLong("film_id")),
                id);
    }

    @Override
    public ResponseEntity<Film> createFilm(Film film) {
        FilmRateValidator.filmValid(film);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("film_id");

        long idReturn = simpleJdbcInsert.executeAndReturnKey(getMapFromFilm(film)).longValue();

        setGenres(idReturn, film.getGenres());
        setDirectors(idReturn, film.getDirectors());

        film.setId(idReturn);
        return new ResponseEntity<>(film, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Film> updateFilm(Film film) {
        FilmRateValidator.filmValid(film);

        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, " +
                "duration = ?, rating_id = ?" +
                "WHERE film_id = ?";

        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());

        if (result == 1) {
            long filmId = film.getId();
            setGenres(filmId, film.getGenres());
            setDirectors(filmId, film.getDirectors());
            setLikes(filmId, film.getLikes());

            film = getFilmById(filmId);
            return new ResponseEntity<>(film, HttpStatus.OK);
        } else {
            log.warn("Фильм с id=" + film.getId() + " не обновлён, нет в базе");
            throw new NoDataFoundException("Фильма с id=" + film.getId() + " нет в базе");
        }

    }

    @Override
    public void deleteFilm(Long id) {
        String sqlQuery = "DELETE FROM films WHERE film_id = ?";

        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            log.warn("Фильма с id=" + id + " нет в базе");
            throw new NoDataFoundException("Фильма с id=" + id + " нет в базе");
        }
    }

    @Override
    public Film getFilmById(Long id) {
        String sqlQuery = "SELECT * FROM films WHERE film_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Фильма с id=" + id + " нет в базе");
            throw new NoDataFoundException("Фильма с id=" + id + " нет в базе");
        }
    }

    @Override
    public List<Film> getCommonFilms(Long userId, Long friendId) {
        String sqlQuery = "SELECT film_id FROM likes WHERE user_id = ? " +
                "INTERSECT SELECT film_id FROM likes WHERE user_id = ?";
        try {
            return jdbcTemplate.query(sqlQuery,
                    (resultSet, rowNum) -> getFilmById(resultSet.getLong("film_id")), userId, friendId);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        Long filmId = resultSet.getLong("film_id");

        return Film.builder()
                .id(filmId)
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .releaseDate(resultSet.getDate("release_date").toLocalDate())
                .duration(resultSet.getInt("duration"))
                .mpa(getMpa(filmId))
                .genres(getGenres(filmId))
                .likes(new LinkedHashSet<>(getLikes(filmId)))
                .directors(getDirectors(filmId))
                .build();

    }

    private Mpa getMpa(Long id) {
        String sqlQuery = "SELECT rating_id, name FROM ratings  " +
                "WHERE rating_id = (SELECT rating_id " +
                "FROM films " +
                "WHERE film_id = ?);";

        try {
            return jdbcTemplate.queryForObject(sqlQuery,
                    (rs, rowNum) -> new Mpa(rs.getInt("rating_id"),
                            rs.getString("name")),
                    id);
        } catch (EmptyResultDataAccessException e) {
            return new Mpa();
        }
    }

    private List<Genre> getGenres(Long id) {
        String sqlQuery = "SELECT g.genre_id, g.name \n" +
                "FROM genres g \n" +
                "WHERE g.genre_id IN (SELECT genre_id \n" +
                "                     FROM film_genre fg \n" +
                "                     WHERE film_id = ?);";

        try {
            return jdbcTemplate.query(sqlQuery,
                    (rs, rowNum) -> new Genre(rs.getInt("genre_id"),
                            rs.getString("name")),
                    id);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private List<Director> getDirectors(Long id) {
        String sqlQuery = "SELECT d.director_id, d.name \n" +
                "FROM directors d \n" +
                "WHERE d.director_id IN (SELECT director_id \n" +
                "                        FROM film_director fd \n" +
                "                        WHERE film_id = ?);";

        try {
            return jdbcTemplate.query(sqlQuery,
                    (rs, rowNum) -> new Director(rs.getLong("director_id"),
                            rs.getString("name")), id);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private void setGenres(Long id, List<Genre> genres) {
        removeGenres(id);

        Set<Integer> genresId = genres.stream()
                .map(Genre::getId)
                .collect(Collectors.toSet());

        if (genresId.size() > 0) {
            for (Integer genreId : genresId) {
                String sqlQuery = "INSERT INTO film_genre (film_id, genre_id) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, id, genreId);
            }
        }
    }

    private void removeGenres(Long id) {
        String sqlQuery = "DELETE FROM film_genre WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    private void setDirectors(Long id, List<Director> directors) {
        removeDirectors(id);

        if (directors.size() > 0) {
            for (Director director : directors) {
                String sqlQuery = "INSERT INTO film_director (film_id, director_id) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, id, director.getId());
            }
        }
    }

    private void removeDirectors(Long id) {
        String sqlQuery = "DELETE FROM film_director WHERE film_id = ?;";
        jdbcTemplate.update(sqlQuery, id);
    }

    private List<Long> getLikes(Long id) {
        String sqlQuery = "SELECT user_id FROM likes WHERE film_id = ?";
        try {
            return jdbcTemplate.query(sqlQuery,
                    (resultSet, rowNum) -> resultSet.getLong("user_id"), id);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private void setLikes(Long id, Set<Long> likes) {
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?", id);
        if (likes.size() > 0) {
            for (Long like : likes) {
                String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, id, like);
            }
        }
    }

    private Map<String, Object> getMapFromFilm(Film film) {
        Map<String, Object> values = new HashMap<>();
        values.put("name", film.getName());
        values.put("description", film.getDescription());
        values.put("release_date", film.getReleaseDate());
        values.put("duration", film.getDuration());
        values.put("rating_id", film.getMpa().getId());

        return values;
    }
}