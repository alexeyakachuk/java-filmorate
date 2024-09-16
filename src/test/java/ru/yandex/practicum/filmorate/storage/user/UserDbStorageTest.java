package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final UserStorage userStorage;
    private final NamedParameterJdbcOperations jdbcOperations;
    private User testUser;
    private User testUser2;

    @BeforeEach
    void setUp() {
        jdbcOperations.update("DELETE FROM friendships", Map.of());
        jdbcOperations.update("DELETE FROM users", Map.of());
        User user = User.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        testUser = userStorage.createUser(user);

        User user1 = User.builder()
                .email("test@example1.com")
                .login("testlogin1")
                .name("Test User1")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();

        testUser2 = userStorage.createUser(user1);
    }

    @Test
    void findAllUsersTest() {
        List<User> allUsers = userStorage.findAllUsers();
        userStorage.addFriend(testUser.getId(), testUser2.getId());
        assertEquals(2, allUsers.size());
    }

    @Test
    void findUserTest() {
        User findUser = userStorage.findUser(testUser.getId());
        assertNotNull(findUser);
        assertEquals(testUser, findUser, "Пользователи должны совподать");
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
    void updateUserTest() {
        User user1 = User.builder()
                .email("test@example.com")
                .login("testlogin")
                .name("Test User")
                .birthday(LocalDate.of(1990, 1, 1))
                .build();
        user1.setId(testUser.getId());
        User user2 = userStorage.updateUser(user1);

        assertEquals(user1, user2);
    }

    @Test
    void addFriendTest() {
        userStorage.addFriend(testUser.getId(), testUser2.getId());
        List<Long> testUserFriends = userStorage.getFriendsByUserId(testUser.getId());
        List<Long> testUser2Friends = userStorage.getFriendsByUserId(testUser2.getId());

        assertEquals(1, testUserFriends.size());
        assertEquals(0, testUser2Friends.size());
    }

    @Test
    void deleteFriendTest() {
        userStorage.addFriend(testUser.getId(), testUser2.getId());
        userStorage.deleteFriend(testUser.getId(), testUser2.getId());
        List<User> allUserFriends = userStorage.getUserFriends(testUser.getId());

        assertEquals(0, allUserFriends.size());
    }

    @Test
    void getUserFriendsTest() {
        userStorage.addFriend(testUser.getId(), testUser2.getId());
        List<User> allUserFriends = userStorage.getUserFriends(testUser.getId());

        assertEquals(1, allUserFriends.size());
        assertEquals(testUser2, allUserFriends.getFirst());
    }
}