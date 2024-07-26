INSERT INTO MPA (NAME)
VALUES ('G');
INSERT INTO MPA (NAME)
VALUES ('PG');
INSERT INTO MPA (NAME)
VALUES ('PG-13');
INSERT INTO MPA (NAME)
VALUES ('R');
INSERT INTO MPA (NAME)
VALUES ('NC-17');

INSERT INTO GENRES (NAME)
VALUES ('Комедия');
INSERT INTO GENRES (NAME)
VALUES ('Драма');
INSERT INTO GENRES (NAME)
VALUES ('Мультфильмы');
INSERT INTO GENRES (NAME)
VALUES ('Триллер');
INSERT INTO GENRES (NAME)
VALUES ('Боевик');
INSERT INTO GENRES (NAME)
VALUES ('Документальный');
INSERT INTO GENRES (NAME)
VALUES ('Ужасы');
INSERT INTO GENRES (NAME)
VALUES ('Мистика');
INSERT INTO GENRES (NAME)
VALUES ('Романтика');
INSERT INTO GENRES (NAME)
VALUES ('Фантастика');

-- Добавление первого пользователя
INSERT INTO USERS (email, login, name, birthday)
VALUES ('john@example.com', 'john_doe', 'John Doe', '1990-05-15');

-- Добавление второго пользователя
INSERT INTO USERS (email, login, name, birthday)
VALUES ('jane@example.com', 'jane_smith', 'Jane Smith', '1985-09-22');

-- Добавление первого фильма
INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
VALUES ('Собачье сердце', 'Фильм по мотивам повести Михаила Булгакова', '1988-07-12', 136, 1);

-- Добавление второго фильма
INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
VALUES ('Левиафан', 'Драма о жизни в современной России', '2014-05-23', 141, 4);

-- Добавление жанра для первого фильма
INSERT INTO FILM_GENRE (film_id, genre_id)
VALUES (1, 1); -- жанр "Драма" для фильма "Собачье сердце"

-- Добавление жанра для второго фильма
INSERT INTO FILM_GENRE (film_id, genre_id)
VALUES (2, 4); -- жанр "Драма" для фильма "Левиафан"