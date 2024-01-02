package ru.yandex.practicum.filmorate.storage;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)

public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    Film film;
    FilmDbStorage filmDbStorage;

    @BeforeEach
    public void createData() {
        // Подготавливаем данные для теста
        film = new Film(1L, "Film1", "Description",
                LocalDate.of(2000, 1, 1),
                120, new Mpa(1, "G"), new ArrayList<>(),  new ArrayList<>(),
                new LinkedHashSet<>());

        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        filmDbStorage.createFilm(film);
    }

    /**
     * Тест метода getFilmById
     */
    @Test
    public void testFindFilmById() {
        Film savedFilm = filmDbStorage.getFilmById(1L);

        assertThat(savedFilm)
                .isNotNull()
                .usingRecursiveComparison()  // проверяем, что значения полей нового
                .isEqualTo(film); // и сохраненного фильма - совпадают
    }

    /**
     * getAllFilms
     */
    @Test
    public void testGetAllFilms() {
        Film secondFilm = new Film(2L, "Film2", "Description",
                LocalDate.of(2000, 1, 1),
                120, new Mpa(1, "G"), new ArrayList<>(), new ArrayList<>(),
                new LinkedHashSet<>());

        filmDbStorage.createFilm(secondFilm);
        List<Film> films = filmDbStorage.getAllFilms();

        assertEquals(2, films.size());
    }

    /**
     * updateFilm
     */
    @Test
    public void testUpdateFilm() {
        Film savedFilm = filmDbStorage.getFilmById(1L);

        savedFilm.setName("Update name"); // Меняем имя фильму, полученному из БД
        filmDbStorage.updateFilm(savedFilm); // Обновлеям
        Film filmFromBd = filmDbStorage.getFilmById(1L);

        assertEquals("Update name", filmFromBd.getName()); // Проверяем, что название фильма изменилось
    }
}