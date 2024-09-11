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
VALUES ('Мультфильм');
INSERT INTO GENRES (NAME)
VALUES ('Триллер');
INSERT INTO GENRES (NAME)
VALUES ('Документальный');
INSERT INTO GENRES (NAME)
VALUES ('Боевик');

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
VALUES (2, 4);
INSERT INTO FILM_GENRE (film_id, genre_id)
VALUES (2, 1); -- жанр "Драма" для фильма "Левиафан"


-----------------------------------------------------
---- Добавление третьего фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Война и мир', 'Экранизация романа Льва Толстого', '1966-01-01', 366, 2);
--
---- Добавление жанра для третьего фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (3, 2); -- жанр "Исторический" для фильма "Война и мир"
--
---- Добавление четвертого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Брат', 'История о молодом человеке, вернувшемся с войны', '1997-04-17', 96, 1);
--
---- Добавление жанра для четвертого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (4, 1); -- жанр "Драма" для фильма "Брат"
--
---- Добавление пятого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Ночной дозор', 'Фантастический боевик о противостоянии темных и светлых сил', '2004-01-01', 130, 3);
--
---- Добавление жанра для пятого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (5, 3); -- жанр "Фантастика" для фильма "Ночной дозор"
--
---- Добавление шестого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('12', 'Современная интерпретация судебного разбирательства', '2007-10-11', 159, 2);
--
---- Добавление жанра для шестого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (6, 1); -- жанр "Драма" для фильма "12"
--
---- Добавление седьмого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Сталкер', 'Философская драма о поисках смысла жизни', '1979-04-20', 163, 2);
--
---- Добавление жанра для седьмого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (7, 1); -- жанр "Драма" для фильма "Сталкер"
--
---- Добавление восьмого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Завтра война', 'История о том, как готовиться к войне', '1983-01-01', 90, 1);
--
---- Добавление жанра для восьмого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (8, 2); -- жанр "Военный" для фильма "Завтра война"
--
---- Добавление девятого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Кин-дза-дза!', 'Комедия о путешествии на другую планету', '1986-12-01', 100, 3);
--
---- Добавление жанра для девятого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (9, 4); -- жанр "Комедия" для фильма "Кин-дза-дза!"
--
---- Добавление десятого фильма
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Территория', 'Фильм о поисках себя в жизни', '2015-02-12', 120, 2);
--
---- Добавление жанра для десятого фильма
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (10, 1); -- жанр "Драма" для фильма "Территория"
--
---- Продолжаем добавлять фильмы до тридцатого
--
---- Одиннадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Сумасшедшая помощь', 'Комедия о неудачливом психотерапевте', '2019-11-15', 95, 4);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (11, 4);
--
---- Двенадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Секреты кино', 'Документальный фильм о создании кино', '2020-05-20', 85, 2);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (12, 5);
--
---- Тринадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Лето', 'Фильм о любви и музыке в Советском Союзе', '2018-06-14', 120, 1);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (13, 1);
--
---- Четырнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Дурак', 'Социальная драма о коррупции в России', '2014-12-04', 116, 2);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (14, 1);
--
---- Пятнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Светлая сторона жизни', 'История о надежде и любви', '2019-09-10', 102, 3);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (15, 1);
--
---- Шестнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Мой друг Иван Лапшин', 'Фильм о детстве и дружбе', '1984-05-20', 90, 2);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (16, 1);
--
---- Семнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Пять вечеров', 'Любовная история в советское время', '1979-12-01', 100, 1);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (17, 1);
--
---- Восемнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Кукушка', 'Фильм о любви на фоне войны', '2007-09-21', 110, 2);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (18, 1);
--
---- Девятнадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('Иван Васильевич меняет профессию', 'Комедия о путешествии во времени', '1973-12-31', 95, 4);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (19, 4);
--
---- Двадцатый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('О чем говорят мужчины', 'Комедия о мужской дружбе и жизни', '2010-02-11', 80, 4);
--INSERT INTO FILM_GENRE (film_id, genre_id)
--VALUES (20, 4);
--
---- Двадцать первый фильм
--INSERT INTO FILMS (name, description, release_date, duration, mpa_id)
--VALUES ('А зори здесь тихие...', 'Военная драма о дружбе и подвиге', '1972-03-23', 100 ,2);
--INSERT INTO FILM_GENRE (film_id ,genre_id )
--VALUES(21 ,2 );
--
---- Двадцать второй фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Кавказская пленница' ,'Комедия о приключениях' ,'1967-01-01' ,90 ,4 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(22 ,4 );
--
---- Двадцать третий фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Гараж' ,'Комедия о жизни в советском обществе' ,'1979-01-01' ,90 ,4 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(23 ,4 );
--
---- Двадцать четвертый фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Служебный роман' ,'Романтическая комедия' ,'1977-12-31' ,90 ,4 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(24 ,4 );
--
---- Двадцать пятый фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Ширли-мырли' ,'Комедия о приключениях' ,'1995-01-01' ,90 ,4 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(25 ,4 );
--
---- Двадцать шестой фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Трудно быть богом' ,'Фантастическая драма' ,'2013-04-01' ,140 ,2 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(26 ,3 );
--
---- Двадцать седьмой фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Ностальгия' ,'Драма о поисках утраченного времени' ,'1983-01-01' ,118 ,2 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(27 ,1 );
--
---- Двадцать восьмой фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Летят журавли' ,'Военная драма о любви' ,'1957-03-16' ,97 ,2 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(28 ,2 );
--
---- Двадцать девятый фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Москва слезам не верит' ,'Романтическая драма' ,'1980-12-27' ,143 ,1 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(29 ,1 );
--
---- Тридцатый фильм
--INSERT INTO FILMS(name ,description ,release_date ,duration ,mpa_id )
--VALUES('Остров' ,'Философская драма о вере и искуплении' ,'2006-10-05' ,110 ,2 );
--INSERT INTO FILM_GENRE(film_id ,genre_id )
--VALUES(30 ,1 );
--
