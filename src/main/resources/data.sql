INSERT INTO MPA (RATING)
VALUES ('G');
INSERT INTO MPA (RATING)
VALUES ('PG');
INSERT INTO MPA (RATING)
VALUES ('PG-13');
INSERT INTO MPA (RATING)
VALUES ('R');
INSERT INTO MPA (RATING)
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