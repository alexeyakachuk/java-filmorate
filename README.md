# java-filmorate
Template repository for Filmorate project.
# ER diagram
![Описание картинки](https://raw.githubusercontent.com/alexeyakachuk/images/main/Untitled.png)
## Пояснение к схеме
> users: таблица пользователей, содержит информацию о каждом пользователе.
> films: таблица фильмов, содержит информацию о каждом фильме.
> friendships: таблица дружбы, содержит информацию о дружеских связях пользователей.
> MPA: таблица рейтингов MPA, содержит информацию о рейтингач фильмов.
> genres: таблица жанров, содержит информацию о жанрах фильмов.
> film_genere: таблица связей фильмов и жанров, содержит информацию о том, к каким жанрам относятся фильмы.
> likes: таблица лайков, содержит информацию о том, какие пользователи лайкнули фильм.
## Примеры запросов
### Получение всех пользователей
>SELECT * FROM USERS;
### Получение всех фильмов
>SELECT * FROM films;
### Получение всех фильмов, которые лайкнул пользователь
>SELECT films.*
>FROM films
>JOIN likes ON films.id = likes.film_id
>WHERE likes.user_id = 1;