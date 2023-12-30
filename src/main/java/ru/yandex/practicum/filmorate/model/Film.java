package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

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

}
