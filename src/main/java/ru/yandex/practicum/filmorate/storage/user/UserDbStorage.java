package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
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

import java.util.*;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserRowMapper mapper;
    private final NamedParameterJdbcOperations jdbcOperations;

    @Override
    public List<User> findAllUsers() {
        String query = "SELECT users.*, friendships.from_user_id FROM users " +
                "LEFT JOIN friendships ON users.id = friendships.from_user_id";
        return jdbcTemplate.query(query, mapper);

    }
@Override
public Map<Long, Set<Long>> getAllFriends() {
        String query = "SELECT from_user_id, to_user_id FROM friendships";
        Map<Long, Set<Long>> friendsByUserId = new HashMap<>();

        List<Map<String, Object>> rows = jdbcTemplate.queryForList(query);
        for (Map<String, Object> row : rows) {
            Long fromUserId = (Long) row.get("from_user_id");
            Long toUserId = (Long) row.get("to_user_id");

            friendsByUserId.computeIfAbsent(fromUserId, k -> new HashSet<>()).add(toUserId);
//            friendsByUserId.computeIfAbsent(toUserId, k -> new HashSet<>()).add(fromUserId);
        }

        return friendsByUserId;
    }

//    SELECT USERS.id, USERS.email, USERS.login, USERS.name, USERS.birthday, FRIENDSHIPS.from_user_id, FRIENDSHIPS.to_user_id
//    FROM USERS
//    LEFT JOIN FRIENDSHIPS ON USERS.id = FRIENDSHIPS.from_user_id OR USERS.id = FRIENDSHIPS.to_user_id
//    ORDER BY USERS.id;

    @Override
    public User findUser(long id) {
        String query = "SELECT * FROM users WHERE ID = ?";
        try {
            return jdbcTemplate.queryForObject(query, mapper, id);
        } catch (DataAccessException e) {
            return null;
        }


    }

    @Override
    public User createUser(User newUser) {
        log.info("Создание нового пользователя: {}", newUser);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", newUser.getEmail());
        params.addValue("login", newUser.getLogin());
        params.addValue("name", newUser.getName());
        params.addValue("birthday", newUser.getBirthday());

        jdbcOperations.update(
                "INSERT INTO users(email, login, name, birthday) VALUES (:email, :login, :name, :birthday)",
                params, keyHolder);

        log.info("Пользователь {} сохранен", newUser);

        long userId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        return findUser(userId);
    }


    @Override
    public User updateUser(User newUser) {
        // Проверяем, существует ли пользователь с данным ID
        checkId(newUser);

        // Определяем SQL-запрос с именованными параметрами
        String sql = "UPDATE users SET email = :email, name = :name, login = :login, birthday = :birthday WHERE id = :id";

        // Подготавливаем параметры для запроса
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("id", newUser.getId());
        params.addValue("email", newUser.getEmail());
        params.addValue("name", newUser.getName());
        params.addValue("login", newUser.getLogin());
        params.addValue("birthday", newUser.getBirthday());

        // Выполняем запрос на обновление
        jdbcOperations.update(sql, params);


        return findUser(newUser.getId());
    }

    private void checkId(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        if (findUser(newUser.getId()) == null) {
            throw new NotFoundException("Пользователя с таким id " + newUser.getId() + " нет");
        }

    }

    // метод добавления в друзей

    @Override
    public void addFriend(long fromUserId, long toUserId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("fromUserId", fromUserId);
        params.addValue("toUserId", toUserId);
        jdbcOperations.update("MERGE INTO friendships (from_user_id, to_user_id) " +
                "VALUES (:fromUserId, :toUserId)", params);
    }

    @Override
    public List<Long> getFriendsByUserId(long userId) {
        String query = "SELECT friendships.to_user_id FROM friendships " +
                "WHERE from_user_id = :from_user_id";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("from_user_id", userId);
        List<Long> friends = jdbcOperations.queryForList(query, params, Long.class);
        return friends;
    }

    @Override
    public void deleteFriend(long userId, long friendId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("userId", userId);
        params.addValue("friendId", friendId);

        jdbcOperations.update("DELETE FROM friendships " +
                "WHERE from_user_id = :userId AND to_user_id = :friendId", params);
    }

//    @Override
//    public List<User> getUserFriends(long userId) {
//        List<Long> friendIds = getFriendsByUserId(userId);
//        List<User> friends = new ArrayList<>();
//
//        for (Long friendId : friendIds) {
//            User user = findUser(friendId);
//            friends.add(user);
//        }
//
//        return friends;
//    }
@Override
public List<User> getUserFriends(long userId) {
    String query = "SELECT f.to_user_id FROM friendships f WHERE f.from_user_id = :userId";
    MapSqlParameterSource params = new MapSqlParameterSource();
    params.addValue("userId", userId);
    List<Long> friendIds = jdbcOperations.queryForList(query, params, Long.class);

    // Используем стримы для преобразования списка ID друзей в список объектов User
    return friendIds.stream()
            .map(this::findUser)
            .collect(Collectors.toList());
}
}
