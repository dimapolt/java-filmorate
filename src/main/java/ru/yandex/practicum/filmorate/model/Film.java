package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    /**
     * Данные из сторонних таблиц
     */
    private Mpa mpa; // таблица rating
    private List<Genre> genres; // таблица genre
    private List<Director> directors; // режиссёры фильма

    @Setter(AccessLevel.NONE)
    private Set<Long> likes; // таблица likes

    public Film() {
        mpa = new Mpa();
        genres = new ArrayList<>();
        likes = new LinkedHashSet<>();
        directors = new ArrayList<>();
    }

    public void setLike(Long userId) {
        likes.add(userId);
    }

    public void unSetLike(Long userId) {
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return Objects.equals(id, film.id) && Objects.equals(name, film.name) && Objects.equals(description, film.description) && Objects.equals(releaseDate, film.releaseDate) && Objects.equals(duration, film.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, releaseDate, duration);
    }

}
