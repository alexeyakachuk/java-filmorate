package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.GenreRowMapper;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final NamedParameterJdbcOperations jdbcOperations;
    private final GenreRowMapper mapper;

    @Override
    public List<Genre> findAll() {
        String query = "SELECT * FROM genres";
        return jdbcOperations.query(query, mapper);
    }

    @Override
    public Genre findById(int id) {
        String query = "SELECT * FROM genres WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", id);
        return jdbcOperations.queryForObject(query, params, mapper);

    }

    @Override
    public List<Genre> findByFilmId(long filmId) {
        String query = "SELECT * FROM genres " +
                "JOIN film_genre ON genres.id = film_genre.genre_id " +
                " WHERE film_genre.film_id = :filmId";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("filmId", filmId);
        List<Genre> genres = jdbcOperations.query(query, params, mapper);
        return genres;
    }

    @Override
    public void addGenresToFilm(Film film) {
        Set<Genre> genres = film.getGenres();
        Long filmId = film.getId();
        if (genres != null && !genres.isEmpty()) {
            for (Genre genre : genres) {
                jdbcOperations.update(
                        "MERGE INTO film_genre (film_id, genre_id) VALUES (:film_id, :genre_id)",
                        new MapSqlParameterSource()
                                .addValue("film_id", filmId)
                                .addValue("genre_id", genre.getId()));
            }
        }
    }

    @Override
    public List<Genre> findGenresByFilmId(long filmId) {
        String query = "SELECT genres.* FROM film_genre " +
                "JOIN genres ON film_genre.genre_id = genres.id " +
                "WHERE film_genre.film_id = :film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        List<Genre> genres = jdbcOperations.query(query, params, mapper);
        return genres;
    }

    @Override
    public void removeGenresFromFilm(long filmId) {
        String deleteGenresSql = "DELETE FROM film_genre WHERE film_id = :film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        jdbcOperations.update(deleteGenresSql, params);
    }

}
