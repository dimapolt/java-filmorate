package ru.yandex.practicum.filmorate.storage.event;

import ru.yandex.practicum.filmorate.model.Event;

import java.util.List;

public interface EventStorage {
    List<Event> getAll();

    List<Event> getByUserId(long userId);

    long createEvent(Event.EntityType eventType,
                     long entityId, Event.EventOperationType eventOperationType, long userId);

    void flushForUser(long userId);

    void deleteById(long eventId);

    void deleteByEntityId(Event.EntityType eventType, long entityId);
}
