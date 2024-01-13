package ru.yandex.practicum.filmorate.storage.event;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class EventDbStorage implements EventStorage {
    private final JdbcTemplate jdbcTemplate;
    private final UserStorage userStorage;

    @Autowired
    public EventDbStorage(JdbcTemplate jdbcTemplate, @Qualifier("userDbStorage") UserStorage userStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.userStorage = userStorage;
    }


    @Override
    public List<Event> getAll() {
        String sqlQuery = "SELECT event_Id, eventTimestamp, eventType, eventOperationType, entity_Id " +
                "from EVENTS";

        return mapRowSetToEventList(jdbcTemplate.queryForRowSet(sqlQuery));
    }

    @Override
    public List<Event> getByUserId(long userId) {
        userStorage.getUserById(userId);

        String sqlQuery = "SELECT event_Id, eventTimestamp, eventType, eventOperationType, entity_Id, user_id " +
                "from EVENTS " +
                "WHERE user_Id = ?";

        SqlRowSet userFeed = jdbcTemplate.queryForRowSet(sqlQuery, userId);

        return mapRowSetToEventList(userFeed);
    }

    @Override
    public long createEvent(Event.EntityType eventType, long entityId,
                            Event.EventOperationType eventOperationType, long userId) {

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("EVENTS")
                .usingGeneratedKeyColumns("event_Id");

        return simpleJdbcInsert.executeAndReturnKey(Event.builder()
                .timestamp(Instant.now().toEpochMilli())
                .userId(userId)
                .entityId(entityId)
                .eventType(eventType)
                .operation(eventOperationType)
                .build().toMap()).longValue();
    }

    @Override
    public void deleteByEntityId(Event.EntityType entityType, long entityId) {
        String sqlQuery =
                "DELETE FROM EVENTS WHERE ENTITYTYPE =? AND ENTITY_ID =?";

        jdbcTemplate.update(sqlQuery, entityType, entityId);
    }

    @Override
    public void flushForUser(long userId) {
        String sqlQuery =
                "DELETE FROM EVENTS WHERE USER_ID =?";

        jdbcTemplate.update(sqlQuery, userId);
    }

    @Override
    public void deleteById(long eventId) {
        String sqlQuery =
                "DELETE FROM EVENTS WHERE EVENT_ID =?";

        jdbcTemplate.update(sqlQuery, eventId);
    }

    private Event mapRowToEvent(ResultSet resultSet, int rowNum) throws SQLException {
        return Event.builder()
                .eventId(resultSet.getLong("event_Id"))
                .timestamp(resultSet.getLong("eventTimestamp"))
                .userId(resultSet.getLong("user_id"))
                .operation(Event.EventOperationType.valueOf(resultSet.getString("eventOperationType")))
                .eventType(Event.EntityType.valueOf(resultSet.getString("eventType")))
                .entityId(resultSet.getLong("entity_Id"))
                .build();
    }

    private List<Event> mapRowSetToEventList(SqlRowSet sqlRowSet) {
        List<Event> userFeedList = new ArrayList<>();

        while (sqlRowSet.next()) {
            userFeedList.add(Event.builder()
                    .eventId(sqlRowSet.getLong("event_Id"))
                    .timestamp(sqlRowSet.getLong("eventTimestamp"))
                    .userId(sqlRowSet.getLong("user_id"))
                    .eventType(Event.EntityType.valueOf(sqlRowSet.getString("eventType")))
                    .entityId(sqlRowSet.getLong("entity_Id"))
                    .operation(Event.EventOperationType.valueOf(sqlRowSet.getString("eventOperationType")))
                    .build()
            );

        }
        return userFeedList;
    }

}
