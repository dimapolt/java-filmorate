package ru.yandex.practicum.filmorate.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
@EqualsAndHashCode
public class Review {
    private long reviewId;

    @NotNull
    private String content;

    @NotNull
    private Boolean isPositive;

    @Positive
    private long userId;

    @Positive
    private long filmId;
    private int useful;

    public Review(long reviewIdArg, String contentArg, boolean isPositiveArg,
                  long filmIdArg, long userIdArg, int usefulArg) {
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
