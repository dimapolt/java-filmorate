DROP TABLE IF EXISTS film_genre, genres, likes, films, ratings, friendship, users, directors, film_director;

CREATE TABLE IF NOT EXISTS ratings (
	rating_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(10)
);

CREATE TABLE IF NOT EXISTS films (
	film_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(255) NOT NULL,
	description VARCHAR(255),
	release_date TIMESTAMP,
	duration INTEGER NOT NULL,
	rating_id INTEGER NOT NULL REFERENCES ratings (rating_id)
);

CREATE TABLE IF NOT EXISTS directors (
	director_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS film_director (
    film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
	director_id INTEGER NOT NULL REFERENCES directors (director_id) ON DELETE CASCADE,
	CONSTRAINT film_director_pk PRIMARY KEY(film_id, director_id)
);

CREATE TABLE IF NOT EXISTS genres (
	genre_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	name VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS film_genre (
	film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
	genre_id INTEGER NOT NULL REFERENCES genres (genre_id) ON DELETE CASCADE,
	CONSTRAINT film_genre_pk PRIMARY KEY(film_id, genre_id)
);

CREATE TABLE IF NOT EXISTS users (
	user_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	email VARCHAR(50),
	login VARCHAR(50) NOT NULL,
	name  VARCHAR(255) NOT NULL,
	birthday TIMESTAMP
);

CREATE TABLE IF NOT EXISTS friendship (
	friendship_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
	user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
	friend_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS likes (
	film_id INTEGER NOT NULL REFERENCES films (film_id) ON DELETE CASCADE,
	user_id INTEGER NOT NULL REFERENCES users (user_id) ON DELETE CASCADE,
	CONSTRAINT likes_pk PRIMARY KEY(film_id, user_id)
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    content VARCHAR(255) NOT NULL,
    isPositive BOOLEAN,
    user_id INTEGER REFERENCES users (user_id) ON DELETE CASCADE,
    film_id INTEGER REFERENCES films (film_id) ON DELETE CASCADE,
    useful INTEGER DEFAULT 0
);