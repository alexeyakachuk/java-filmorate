package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
@Repository
@Qualifier("H2UserStorage")
public class UserDbStorage implements UserStorage{

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


//    Statement statement = connection.createStatement();
//    String filmsTableQuery = "SELECT COUNT(*) FROM films";
//    ResultSet results = statement.execute(query);
//results.next();
//    int result = results.getRow();
//statement.close();

    @Override
    public List<User> findAllUsers() {
        return jdbcTemplate.queryForList("SELECT * FROM USERS", User.class);
    }

    @Override
    public User findUser(long id) {
        return null;
    }

    @Override
    public User createUser(User newUser) {
        return null;
    }

    @Override
    public User updateUser(User newUser) {
        return null;
    }
}
