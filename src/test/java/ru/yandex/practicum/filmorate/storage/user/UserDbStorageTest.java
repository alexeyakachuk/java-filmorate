package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;

    @Test
    void findAllUsers() {
    }

    @Test
    void findUserTest() {
        User user = User.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        User createdUser = userStorage.createUser(user);
        User findUser = userStorage.findUser(createdUser.getId());

        assertNotNull(findUser.getId(), "ID пользователя не должен быть null после создания");
        assertEquals(findUser.getId(), createdUser.getId(), "Id должен совпадать");
        assertEquals(findUser.getEmail(), createdUser.getEmail(), "Email должен совпадать");
        assertEquals(findUser.getLogin(), createdUser.getLogin(), "Login должен совпадать");
        assertEquals(findUser.getName(), createdUser.getName(), "Name должен совпадать");
        assertEquals(findUser.getBirthday(), createdUser.getBirthday(), "Birthday должен совпадать");
    }

    @Test
    void createUserTest() {
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

    @Test
    void updateUser() {
    }

    @Test
    void addFriend() {
    }

    @Test
    void getFriendsByUserId() {
    }

    @Test
    void deleteFriend() {
    }

    @Test
    void getUserFriends() {
    }
}