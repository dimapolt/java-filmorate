package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Review;
import ru.yandex.practicum.filmorate.service.ReviewService;

import javax.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    private static final Logger log = LoggerFactory.getLogger(ReviewController.class); // создаем логгер
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewServiceArg) {
        reviewService = reviewServiceArg;
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED) // устанавливаем код ответа 201
    public Review createReview(@Valid @RequestBody Review review) {
        log.info("Пришел POST /reviews запрос с телом: {}", review);
        final Review createdReview = reviewService.createReview(review);
        log.info("На запрос POST /reviews отправлен ответ с телом: {}", createdReview);
        return createdReview;
    }

    @PutMapping
    public Review updateReview(@Valid @RequestBody Review review) {
        log.info("Пришел PUT /reviews запрос с телом: {}", review);
        final Review updatedReview = reviewService.updateReview(review);
        log.info("На запрос PUT /reviews отправлен ответ с телом: {}", updatedReview);
        return updatedReview;
    }

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        log.info("Пришел GET /reviews/{} запрос", id);
        final Review review = reviewService.getReviewById(id);
        log.info("На запрос GET /reviews/{} отправлен ответ с телом: {}", id, review);
        return review;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT) // устанавливаем код ответа 204
    public void deleteReviewById(@PathVariable long id) {
        log.info("Пришел DELETE /reviews/{} запрос", id);
        reviewService.deleteReviewById(id);
        log.info("Отзыв с id={} успешно удален!", id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLikeForFilmReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел PUT /reviews/{}/like/{} запрос", id, userId);
        reviewService.addLikeOrDislikeForFilmReview(id, userId, true);
        log.info("Пользователь с id={} поставил лайк отзыву с id={}", userId, id);
    }

    @PutMapping("/{id}/dislike/{userId}")
    public void addDislikeForFilmReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел PUT /reviews/{}/dislike/{} запрос", id, userId);
        reviewService.addLikeOrDislikeForFilmReview(id, userId, false);
        log.info("Пользователь с id={} поставил дизлайк отзыву с id={}", userId, id);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLikeForFilmReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел DELETE /reviews/{}/like/{} запрос", id, userId);
        reviewService.deleteLikeOrDislikeForFilmReview(id, userId);
        log.info("Пользователь с id={} удалил лайк у отзыва с id={}", userId, id);
    }

    @DeleteMapping("/{id}/dislike/{userId}")
    public void deleteDislikeForFilmReview(@PathVariable long id, @PathVariable long userId) {
        log.info("Пришел DELETE /reviews/{}/dislike/{} запрос", id, userId);
        reviewService.deleteLikeOrDislikeForFilmReview(id, userId);
        log.info("Пользователь с id={} удалил дизлайк у отзыва с id={}", userId, id);
    }
}
