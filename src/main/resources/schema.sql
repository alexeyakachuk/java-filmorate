-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS LIKES;
DROP TABLE IF EXISTS FILM_GENRE;
DROP TABLE IF EXISTS FRIENDSHIPS;
DROP TABLE IF EXISTS USERS;
DROP TABLE IF EXISTS FILMS;
DROP TABLE IF EXISTS GENRES;
DROP TABLE IF EXISTS MPA;

-- Создание таблицы MPA
CREATE TABLE IF NOT EXISTS MPA (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL -- Уменьшение длины, так как название рейтинга обычно короткое
);

-- Создание таблицы жанров
CREATE TABLE IF NOT EXISTS GENRES (
    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(50) NOT NULL -- Уменьшение длины, так как названия жанров обычно короткие
);

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS USERS (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(100) NOT NULL, -- Уменьшение длины для email
    login VARCHAR(50) NOT NULL, -- Уменьшение длины для логина
    name VARCHAR(100) NOT NULL, -- Уменьшение длины для имени
    birthday DATE NOT NULL
);

-- Создание таблицы фильмов
CREATE TABLE IF NOT EXISTS FILMS (
    id BIGINT NOT NULL PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(150) NOT NULL, -- Увеличение длины для названия фильма
    description VARCHAR(500), -- Увеличение длины для описания фильма
    release_date DATE NOT NULL,
    duration INTEGER,
    mpa_id INTEGER,
    CONSTRAINT fk_films_mpa FOREIGN KEY (mpa_id) REFERENCES MPA(id),
    CONSTRAINT duration_positive CHECK(duration > 0)
);

-- Создание таблицы связи фильм-жанр
CREATE TABLE IF NOT EXISTS FILM_GENRE (
    film_id BIGINT NOT NULL,
    genre_id INTEGER NOT NULL,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES FILMS(id),
    FOREIGN KEY (genre_id) REFERENCES GENRES(id)
);

-- Создание таблицы лайков
CREATE TABLE IF NOT EXISTS LIKES (
    film_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    PRIMARY KEY (film_id, user_id),
    FOREIGN KEY (film_id) REFERENCES FILMS(id),
    FOREIGN KEY (user_id) REFERENCES USERS(id)
);

-- Создание таблицы дружбы
CREATE TABLE IF NOT EXISTS FRIENDSHIPS (
    from_user_id BIGINT NOT NULL,
    to_user_id BIGINT NOT NULL,
    PRIMARY KEY (from_user_id, to_user_id),
    FOREIGN KEY (from_user_id) REFERENCES USERS(id),
    FOREIGN KEY (to_user_id) REFERENCES USERS(id)
);






