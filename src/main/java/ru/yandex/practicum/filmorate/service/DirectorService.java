package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorStorage;
import ru.yandex.practicum.filmorate.utils.DirectorValidator;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DirectorService {
    private final DirectorStorage directorStorage;

    public DirectorService(@Qualifier("directorDbStorage") DirectorStorage directorStorage) {
        this.directorStorage = directorStorage;
    }

    public List<Director> getAllDirectors() {
        return directorStorage.getAllDirectors();
    }

    public Director getDirectorById(Long id) {
        return directorStorage.getDirectorById(id);
    }

    public ResponseEntity<Director> createDirector(Director director) {
        DirectorValidator.checkDirectorValid(director);
        return directorStorage.createDirector(director);
    }

    public ResponseEntity<Director> updateDirector(Director director) {
        DirectorValidator.checkDirectorValid(director);
        return directorStorage.updateDirector(director);
    }

    public String deleteDirector(Long id) {
        Optional<Director> director = Optional.ofNullable(directorStorage.getDirectorById(id));
        String message;

        if (director.isEmpty()) {
            message = "Режиссёра с id=" + id + "нет в базе";
            log.warn(message);
            throw new NoDataFoundException(message);
        } else {
            directorStorage.deleteDirector(id);
            message = "Режиссёр с id=" + id + "удалён из базы данных";
            log.info(message);
            return message;
        }

    }
}
