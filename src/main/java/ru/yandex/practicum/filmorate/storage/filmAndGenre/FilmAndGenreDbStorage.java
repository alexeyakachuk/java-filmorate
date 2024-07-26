package ru.yandex.practicum.filmorate.storage.filmAndGenre;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.mappers.FilmAndGenreRowMapper;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Qualifier("H2FilmAndGenreStorage")
public class FilmAndGenreDbStorage implements FilmAndGenreStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmAndGenreRowMapper mapper;

    @Override
    public List<FilmAndGenre> findAllFilmsAndGenre() {
        String query = "SELECT * FROM film_genre";
        return jdbcTemplate.query(query, mapper);
    }
}
