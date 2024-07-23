package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

@Repository
@Qualifier("H2UserStorage")
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<User> findAllUsers() {
        String query = "SELECT * FROM users";
        return jdbcTemplate.query(query, mapper);
    }

    @Override
    public User findUser(long id) {
        String query = "SELECT * FROM users WHERE ID = ?";
        if (jdbcTemplate.query(query, mapper, id).isEmpty()) {
            return null;
        } else {
            return jdbcTemplate.queryForObject(query, mapper, id);
        }
    }

    @Override
    public User createUser(User newUser) {
        //log.info("Создание нового пользователя: {}", user);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource map = new MapSqlParameterSource();
        map.addValue("email", newUser.getEmail());
        map.addValue("login", newUser.getLogin());
        map.addValue("name", newUser.getName());
        map.addValue("birthday", newUser.getBirthday());

        jdbcOperations.update(
                "INSERT INTO users(email, login, name, birthday) VALUES (:email, :login, :name, :birthday)",
                map, keyHolder);

        //log.info("Пользователь {} сохранен", user);


        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", mapper, keyHolder.getKey().longValue());
    }

    @Override
    public User updateUser(User newUser) {
        checkId(newUser);
        String sql = "UPDATE USERS SET EMAIL = ?, NAME = ?, LOGIN = ?, BIRTHDAY = ? WHERE ID = ?";
        jdbcTemplate.update(sql, newUser.getEmail(), newUser.getName(), newUser.getLogin(), newUser.getBirthday(), newUser.getId());
        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE ID = ?", mapper, newUser.getId());
    }


    private void checkId(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        if (findUser(newUser.getId()) == null) {
            throw new NotFoundException("Пользователя с таким id " + newUser.getId() + " нет");
        }

    }
}
