package ru.yandex.practicum.filmorate.storage.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.utils.FilmRateValidator;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<User> getAllUsers() {
        String sqlQuery = "SELECT * FROM users;";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        FilmRateValidator.userValid(user);

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");

        long idReturn = simpleJdbcInsert.executeAndReturnKey(getMapFromUser(user)).longValue();

        user.setId(idReturn);
        updateFriends(idReturn, user.getFriendsId());
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<User> updateUser(User user) {
        FilmRateValidator.userValid(user);

        String sqlQuery = "UPDATE users SET " +
                "email = ?, login  = ?, " +
                "name = ?, birthday = ?" +
                "WHERE user_id = ?";


        int result = jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                user.getBirthday(),
                user.getId());

        if (result == 1) {
            long userId = user.getId();
            updateFriends(userId, user.getFriendsId());

            user = getUserById(userId);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            log.warn("Фильм с id=" + user.getId() + " не обновлён, нет в базе");
            throw new NoDataFoundException("Фильма с id=" + user.getId() + " нет в базе");
        }

    }

    @Override
    public void deleteUser(Long id) {
        String sqlQuery = "DELETE FROM users WHERE user_id = ?";

        if (jdbcTemplate.update(sqlQuery, id) == 0) {
            log.warn("Пользователя с id=" + id + " нет в базе");
            throw new NoDataFoundException("Пользователя с id=" + id + " нет в базе");
        }
    }

    @Override
    public User getUserById(Long id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        try {
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToUser, id);
        } catch (EmptyResultDataAccessException e) {
            log.warn("Пользователя с id=" + id + " нет в базе");
            throw new NoDataFoundException("Пользователя с id=" + id + " нет в базе");
        }
    }

    @Override
    public boolean removeFriend(Long id, Long friendId) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ? AND friend_id = ?";
        return jdbcTemplate.update(sqlQuery, id, friendId) > 0;
    }

    @Override
    public List<Film> getRecommendedFilms(Long id) {
        User currentUser = getUserById(id);
        List<Film> currentUserLikedFilms = getLikedFilms(currentUser);
        Map<Long, User> users = getAllUsers().stream()
                .collect(Collectors.toMap(User::getId, user -> user));
        Map<Film, Integer> scores = new HashMap<>();

        for (User otherUser : users.values()) {
            if (!otherUser.getId().equals(id)) {
                int similarityIndex = getSimilarity(currentUser, otherUser);
                List<Film> otherUserLikedFilms = getLikedFilms(otherUser);

                for (Film likedFilm : otherUserLikedFilms) {
                    if (!currentUserLikedFilms.contains(likedFilm)) {
                        scores.merge(likedFilm, similarityIndex, Integer::sum);
                    }
                }
            }
        }

        List<Film> recommendedFilms = new ArrayList<>(scores.keySet());
        recommendedFilms.sort(Comparator.comparingInt(key -> scores.get(key)).reversed());

        return recommendedFilms;
    }

    private int getSimilarity(User currentUser, User otherUser) {
        Set<Film> intersection = new HashSet<>();
        intersection.addAll(getLikedFilms(currentUser));
        intersection.retainAll(getLikedFilms(otherUser));

        Set<Film> united = new HashSet<>();
        united.addAll(getLikedFilms(currentUser));
        united.addAll(getLikedFilms(otherUser));

        if (united.isEmpty()) {
            return 0;
        } else {
            return (int) ((double) intersection.size() / united.size() * 100); //Коэффициент Жаккара
        }
    }

    private List<Film> getLikedFilms(User user) {
        FilmStorage filmStorage = new FilmDbStorage(jdbcTemplate);
        return filmStorage.getFilmsByUser(user.getId());
    }

    private User mapRowToUser(ResultSet resultSet, int i) throws SQLException {
        Long id = resultSet.getLong("user_id");
        return User.builder()
                .id(id)
                .email(resultSet.getString("email"))
                .login(resultSet.getString("login"))
                .name(resultSet.getString("name"))
                .birthday(resultSet.getDate("birthday").toLocalDate())
                .friends(new LinkedHashSet<>(getFriends(id)))
                .build();
    }

    private List<Long> getFriends(Long id) {
        String sqlQuery = "SELECT friend_id FROM friendship WHERE user_id = ?";

        try {
            return jdbcTemplate.query(sqlQuery,
                    (resultSet, rowNum) -> resultSet.getLong("friend_id"), id);
        } catch (EmptyResultDataAccessException e) {
            return new ArrayList<>();
        }
    }

    private void updateFriends(Long id, List<Long> newFriends) {
        if (newFriends.size() == 0) { // Если новых друзей нет, то нечего обновлять
            return;
        }

        List<Long> oldFriends = getFriends(id); // Получаем список id друзей из базы
        Set<Long> resultFriends = new HashSet<>(); // Set для получения только уникальных id

        resultFriends.addAll(newFriends); // Добавляем всех новых друзей
        resultFriends.addAll(oldFriends); // Добавляем всех старых друзей (из базы)

        deleteAllFriends(id); // Удаляем из базы информацию о друзьях
        addAllFriends(id, resultFriends); // Добавляем всех друзей обратно в базу
    }

    private void addAllFriends(Long id, Set<Long> friends) {
        for (Long friendId : friends) {
            String sqlQuery = "INSERT INTO friendship (user_id, friend_id) VALUES (?,?);";
            jdbcTemplate.update(sqlQuery, id, friendId);
        }
    }

    private void deleteAllFriends(Long id) {
        String sqlQuery = "DELETE FROM friendship WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery, id);
    }


    private Map<String, Object> getMapFromUser(User user) {
        Map<String, Object> values = new HashMap<>();
        values.put("email", user.getEmail());
        values.put("login", user.getLogin());
        values.put("name", user.getName());
        values.put("birthday", user.getBirthday());

        return values;
    }
}
