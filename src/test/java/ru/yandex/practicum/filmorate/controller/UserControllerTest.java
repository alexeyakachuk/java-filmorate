package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class UserControllerTest {
   // private final Map<Long, User> users = new HashMap<>();
    private  UserController userController;

    @BeforeEach
    public void beforeEach() {
        userController = new UserController(new UserService(new InMemoryUserStorage()));
    }

    @Test
    void createTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        userController.create(user);

        User user1 = User.builder()
                .name(" ")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@mail.ru").build();
        userController.create(user1);

        Collection<User> users = userController.findAll();
        String name = user1.getName();

        assertEquals(2, users.size());
        assertEquals("login", name);

    }

    @Test
    void updateTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        userController.create(user);

        User user1 = User.builder()
                .name("new name")
                .login("newLogin")
                .birthday(LocalDate.of(1990, 10, 17))
                .email("new email@yandex.ru")
                .id(user.getId())
                .build();

        userController.update(user1);

        User updateUser = userController.findAll().getFirst();

        assertEquals("new name", updateUser.getName());
        assertEquals("newLogin", updateUser.getLogin());
        assertEquals(LocalDate.of(1990, 10, 17), updateUser.getBirthday());
        assertEquals("new email@yandex.ru", updateUser.getEmail());
    }

    @Test
    void findAllTest() {
        User user = User.builder()
                .name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        userController.create(user);

        User user1 = User.builder()
                .name(" ")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@mail.ru").build();
        userController.create(user1);

        Collection<User> users = userController.findAll();

        assertEquals(2, users.size());

        List<User> newListUsers = new ArrayList<>(users);

        assertEquals(newListUsers, users);
    }

    @Test
    void addFriendTest() {
        User user1 = User.builder()
                .name("name1")
                .login("login1")
                .birthday(LocalDate.of(1990, 5, 15))
                .email("name1@mail.com")
                .build();
        User user2 = User.builder()
                .name("name2")
                .login("login2")
                .birthday(LocalDate.of(1988, 8, 20))
                .email("name2@.com")
                .build();
        userController.create(user1);
        userController.create(user2);

        userController.addFriend(user1.getId(), user2.getId());

        assertTrue(userController.getFriends(user1.getId()).contains(user2));
        assertTrue(userController.getFriends(user2.getId()).contains(user1));
    }

    @Test
    void deleteFriendTest() {
        User user1 = User.builder()
                .name("name1")
                .login("login1")
                .birthday(LocalDate.of(1990, 5, 15))
                .email("name1@mail.com")
                .build();
        User user2 = User.builder()
                .name("name2")
                .login("login2")
                .birthday(LocalDate.of(1988, 8, 20))
                .email("name2@.com")
                .build();
        userController.create(user1);
        userController.create(user2);

        userController.addFriend(user1.getId(), user2.getId());

        userController.deleteFriend(user1.getId(), user2.getId());

        assertFalse(userController.getFriends(user1.getId()).contains(user2));
        assertFalse(userController.getFriends(user2.getId()).contains(user1));
    }

    @Test
    void findMutualFriendsTest() {
        User user1 = User.builder()
                .name("name1")
                .login("login1")
                .birthday(LocalDate.of(1990, 5, 15))
                .email("name1@mail.com")
                .build();
        User user2 = User.builder()
                .name("name2")
                .login("login2")
                .birthday(LocalDate.of(1988, 8, 20))
                .email("name2@.com")
                .build();
        User user3 = User.builder()
                .name("name3")
                .login("login3")
                .birthday(LocalDate.of(1992, 12, 30))
                .email("name3@mail.com")
                .build();
        userController.create(user1);
        userController.create(user2);
        userController.create(user3);

        userController.addFriend(user1.getId(), user2.getId());
        userController.addFriend(user1.getId(), user3.getId());

        userController.addFriend(user2.getId(), user1.getId());
        userController.addFriend(user2.getId(), user3.getId());

        List<User> mutualFriends = userController.findMutualFriends(user1.getId(), user2.getId());
        assertTrue(mutualFriends.contains(user3));
        assertEquals(1, mutualFriends.size());
    }
}

