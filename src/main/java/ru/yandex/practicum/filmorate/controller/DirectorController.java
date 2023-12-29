package ru.yandex.practicum.filmorate.controller;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.service.DirectorService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/directors")
public class DirectorController {
    private final DirectorService directorService;

    @GetMapping
    public List<Director> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/{id}")
    public Director getDirectorById(@PathVariable Long id) {
        return directorService.getDirectorById(id);
    }

    @PostMapping
    public ResponseEntity<Director> createDirector(@NonNull @RequestBody Director director) {
        return directorService.createDirector(director);
    }

    @PutMapping
    public ResponseEntity<Director> updateDirector(@NonNull @RequestBody Director director) {
        return directorService.updateDirector(director);
    }

    @DeleteMapping("/{id}")
    public String deleteDirector(@PathVariable("id") Long id) {
        return directorService.deleteDirector(id);
    }

}
