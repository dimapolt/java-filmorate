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

    @GetMapping("/{id}")
    public Review getReviewById(@PathVariable long id) {
        log.info("Пришел GET /reviews/{} запрос", id);
        final Review review = reviewService.getReviewById(id);
        log.info("На запрос GET /reviews/{} отправлен ответ с размером тела: 1", id);
        return review;
    }
}
