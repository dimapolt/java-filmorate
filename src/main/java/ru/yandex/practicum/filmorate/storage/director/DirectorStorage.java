package ru.yandex.practicum.filmorate.storage.director;

import org.springframework.http.ResponseEntity;
import ru.yandex.practicum.filmorate.model.Director;

import java.util.List;

public interface DirectorStorage {
    List<Director> getAllDirectors();

    Director getDirectorById(Long id);

    ResponseEntity<Director> createDirector(Director director);

    ResponseEntity<Director> updateDirector(Director director);

    boolean deleteDirector(Long id);
}