package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;

    @Test
    void testCreateUser() {
        User user = User.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User createdUser = userStorage.createUser(user);

        assertNotNull(createdUser.getId(), "ID пользователя не должен быть null после создания");
        assertEquals(user.getEmail(), createdUser.getEmail(), "Email должен совпадать");
        assertEquals(user.getLogin(), createdUser.getLogin(), "Login должен совпадать");
        assertEquals(user.getName(), createdUser.getName(), "Name должен совпадать");
        assertEquals(user.getBirthday(), createdUser.getBirthday(), "Birthday должен совпадать");
    }
}
