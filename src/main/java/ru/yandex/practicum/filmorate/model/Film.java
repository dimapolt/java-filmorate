package ru.yandex.practicum.filmorate.model;

import lombok.*;

import java.time.LocalDate;
import java.util.*;

@Builder
@Data
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

    @Setter(AccessLevel.NONE)
    private Set<Long> likes; // таблица likes

    public Film() {
        mpa = new Mpa();
        genres = new ArrayList<>();
        likes = new LinkedHashSet<>();
    }

    public void setLike(Long userId) {
        likes.add(userId);
    }

    public void unSetLike(Long userId) { likes.remove(userId); }

    public int getLikesCount() {
        return likes.size();
    }

    public int getGenresCount() {return genres.size();}

}
