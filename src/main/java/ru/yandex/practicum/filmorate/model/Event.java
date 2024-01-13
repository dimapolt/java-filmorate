package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Event {
    private long eventId;
    private long timestamp;
    private long userId;
    private EntityType eventType;
    private EventOperationType operation;
    private long entityId;

    public enum EntityType {
        LIKE, REVIEW, FRIEND
    }

    public enum EventOperationType {
        REMOVE, ADD, UPDATE
    }

    public Map<String, Object> toMap() {
        Map<String, Object> values = new HashMap<>();
        values.put("event_Id", eventId);
        values.put("eventTimestamp", timestamp);
        values.put("user_id", userId);
        values.put("eventType", eventType);
        values.put("eventOperationType", operation);
        values.put("entity_Id", entityId);
        return values;
    }

}
