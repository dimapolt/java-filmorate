package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Event;
import ru.yandex.practicum.filmorate.storage.event.EventStorage;

import java.util.List;

@Service
@Slf4j
public class EventService {
    private final EventStorage eventStorage;

    public EventService(@Qualifier("eventDbStorage") EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public List<Event> getAll() {
        return eventStorage.getAll();
    }

    public List<Event> getFeedByUserId(Long userId) {
        return eventStorage.getByUserId(userId);
    }

}
