package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
public class Review {
    private Long reviewId;

    @NotNull
    private String content;

    @NotNull
    private Boolean isPositive;

    @NotNull
    private Long userId;

    @NotNull
    private Long filmId;
    private Integer useful;

    public Review(Long reviewIdArg, String contentArg, Boolean isPositiveArg,
                  Long filmIdArg, Long userIdArg, Integer usefulArg) {
        reviewId = reviewIdArg;
        content = contentArg;
        isPositive = isPositiveArg;
        filmId = filmIdArg;
        userId = userIdArg;
        useful = usefulArg;
    }

    @Override
    public String toString() {
        return "Review{" + "reviewId=" + reviewId + ", content.length='" + content.length() + '\'' +
                ", isPositive=" + isPositive + ", userId=" + userId + ", filmId=" + filmId + ", useful=" + useful +
                "}" + '\n';
    }
}
