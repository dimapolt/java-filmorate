package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.exceptions.NoDataFoundException;
import ru.yandex.practicum.filmorate.model.Director;
import ru.yandex.practicum.filmorate.storage.director.DirectorDbStorage;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class DirectorDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    Director director;
    DirectorDbStorage directorDbStorage;

    @BeforeEach
    public void createData() {
        // Подготавливаем данные для теста
        director = new Director(1L, "Director");

        directorDbStorage = new DirectorDbStorage(jdbcTemplate);
        directorDbStorage.createDirector(director);
    }

    @Test
    public void getDirectorById() {
        Director savedDirector = directorDbStorage.getDirectorById(1L);

        assertThat(savedDirector)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(director);
    }

    @Test
    public void getAllDirectors() {
        Director secondDirector = new Director(2L, "Director2");

        directorDbStorage.createDirector(secondDirector);
        List<Director> directors = directorDbStorage.getAllDirectors();

        assertEquals(2, directors.size());
    }

    @Test
    public void updateDirector() {
        director.setName("Director update");

        directorDbStorage.updateDirector(director);

        Director directorFromDb = directorDbStorage.getDirectorById(1L);
        assertEquals("Director update", directorFromDb.getName());
    }

    @Test
    public void deleteDirector() {
        directorDbStorage.deleteDirector(1L);

        assertThrows(NoDataFoundException.class,
                ()->directorDbStorage.getDirectorById(1L),
                "Режиссёра с id=1 нет в базе");
    }

}
