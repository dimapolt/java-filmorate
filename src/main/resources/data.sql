MERGE INTO rating (rating_id, name)
KEY (rating_id) VALUES (1, 'G'),
                (2, 'PG'),
                (3, 'PG-13'),
                (4, 'R'),
                (5, 'NC-17');
INSERT INTO RATINGS(NAME) VALUES ('G');
INSERT INTO RATINGS(NAME) VALUES ('PG');
INSERT INTO RATINGS(NAME) VALUES ('PG-13');
INSERT INTO RATINGS(NAME) VALUES ('R');
INSERT INTO RATINGS(NAME) VALUES ('NC-17');

INSERT INTO GENRES(NAME) VALUES ('Комедия');
INSERT INTO GENRES(NAME) VALUES ('Драма');
INSERT INTO GENRES(NAME) VALUES ('Мультфильм');
INSERT INTO GENRES(NAME) VALUES ('Триллер');
INSERT INTO GENRES(NAME) VALUES ('Документальный');
INSERT INTO GENRES(NAME) VALUES ('Боевик');

MERGE INTO genre (genre_id, name)
KEY (genre_id) VALUES (1, 'Комедия'),
                (2, 'Драма'),
                (3, 'Мультфильм'),
                (4, 'Триллер'),
                (5, 'Документальный'),
                (6, 'Боевик');
/*
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
			VALUES (1, 'Джентльмены удачи',
			        'Воспитатель детсада внедряется в банду матерых рецидивистов.
                     Евгений Леонов в разошедшейся на цитаты комедии',
			        '1971-12-13', 84,  1);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
			VALUES (2, 'Звёздные войны: Эпизод 1 — Скрытая угроза',
			        'У каждой саги есть начало',
			        '1999-05-16', 136,  2);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
			VALUES (3, 'Гарри Поттер и философский камень',
			        'Путешествие в твою мечту',
			        '2001-11-04', 152,  1);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
			VALUES (4, 'Любовь и голуби',
			        'Вася пытается заслужить прощения у семьи.
                     Мощнейший актерский состав под руководством Владимира Меньшова',
			        '1985-01-07', 107,  3);
INSERT INTO FILMS (FILM_ID, NAME, DESCRIPTION, RELEASE_DATE, DURATION, RATING_ID)
			VALUES (5, 'Один дома',
			        'Новогодняя комедия',
			        '1990-11-10', 103,  1);

INSERT INTO DIRECTORS(director_id, NAME) VALUES (1, 'Владимир Меньшов');
INSERT INTO DIRECTORS(director_id, NAME) VALUES (2, 'Александр Серый');
INSERT INTO DIRECTORS(director_id, NAME) VALUES (3, 'Крис Коламбус');
INSERT INTO DIRECTORS(director_id, NAME) VALUES (4, 'Джордж Лукас');

INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (1, 1);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (1, 2);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (2, 6);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (4, 1);
INSERT INTO FILM_GENRES (FILM_ID, GENRE_ID) VALUES (5, 1);

INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (1, 2);
INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (2, 4);
INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (3, 3);
INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (4, 1);
INSERT INTO FILM_DIRECTOR (FILM_ID, DIRECTOR_ID) VALUES (5, 3);

INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)
			VALUES ('petr@ussr.ru', 'Petr_USSR', 'Petr', '1970-10-10');
INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)
			VALUES ('leo@yandex.ru', 'leoleo', 'Leonid', '1990-07-15');
INSERT INTO USERS(EMAIL, LOGIN, NAME, BIRTHDAY)
			VALUES ('steve@gmail.com', 'st4', 'Steve', '1998-12-01');

INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (1, 1);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (4, 1);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (1, 2);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (2, 2);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (3, 2);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (4, 2);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (5, 2);
INSERT INTO LIKES (FILM_ID, USER_ID) VALUES (3, 3);

INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (1, 2);
INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (2, 1);
INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (2, 3);
INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (1, 3);
INSERT INTO FRIENDSHIP (USER_ID, FRIEND_ID) VALUES (3, 1);
*/
