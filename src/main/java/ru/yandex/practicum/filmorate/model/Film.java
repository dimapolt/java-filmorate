package ru.yandex.practicum.filmorate.model;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Data
public class Film {
    private Long id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Set<Long> likes;

    public Film() {
        likes = new LinkedHashSet<>();
    }

    public void setLike(Long userId) {
        likes.add(userId);
    }

    public void unSetLike(Long userId){
        likes.remove(userId);
    }

    public int getLikesCount() {
        return likes.size();
    }

    public List<Long> getLikes() {
        return new ArrayList<>(likes);
    }
}
