DROP TABLE USERS;
DROP TABLE FILMS;

-- Создание таблицы фильмов
--CREATE TABLE IF NOT EXISTS FILMS (
--id BIGINT PRIMARY KEY AUTO_INCREMENT,
--name VARCHAR(255) NOT NULL,
--description VARCHAR(200),
--release_date INTEGER NOT NULL,
--MPA_ID INTEGER NOT NULL
--);
-- Создание таблицы лайков
CREATE TABLE IF NOT EXISTS MPA (
id INTEGER NOT NULL AUTO_INCREMENT,
rating VARCHAR NOT NULL,
CONSTRAINT MPA_PK PRIMARY KEY (ID)
);

-- Создание таблицы фильмов
CREATE TABLE IF NOT EXISTS FILMS
(
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description varchar(200),
    release_date DATE NOT NULL,
    duration INTEGER,
    mpa_id INTEGER REFERENCES mpa(id),
    CONSTRAINT duration_positive CHECK(duration > 0)
);

-- Создание таблицы жанров
CREATE TABLE IF NOT EXISTS GENRES (
id INTEGER NOT NULL AUTO_INCREMENT,
name VARCHAR(255) NOT NULL,
CONSTRAINT GENRES_PK PRIMARY KEY (ID)
);

-- Создание таблицы лайков
CREATE TABLE IF NOT EXISTS LIKES (
film_id BIGINT NOT NULL,
user_id BIGINT NOT NULL,
CONSTRAINT LIKES_PK PRIMARY KEY (film_id, user_id)
);

-- Создание таблицы жанры фильма
CREATE TABLE IF NOT EXISTS FILM_GENRE (
film_id BIGINT NOT NULL,
genre_id INTEGER NOT NULL
);

--Создание таблицы дружбы
CREATE TABLE IF NOT EXISTS FRIENDSHIPS (
from_user_id BIGINT NOT NULL,
to_user_id BIGINT NOT NULL,
CONSTRAINT FRIENDSHIPS_PK PRIMARY KEY (to_user_id, from_user_id)
);

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL,
    birthday DATE NOT NULL,
    CONSTRAINT USERS_PK PRIMARY KEY (id)
);
