package ru.yandex.practicum.filmorate.storage.likes;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {

    private final NamedParameterJdbcOperations jdbcOperations;


    @Override
    public List<Long> findByIdUserLikes(long filmId) {
        String query = "SELECT user_id FROM likes " +
                "WHERE film_id = :film_id";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("film_id", filmId);
        List<Long> likes = jdbcOperations.queryForList(query, params, Long.class);
        return likes;
    }
}
