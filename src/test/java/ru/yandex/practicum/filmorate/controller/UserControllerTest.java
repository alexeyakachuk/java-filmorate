package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {
    private final Map<Long, User> users = new HashMap<>();


    @Test
    void createTest() {
        UserController userController = new UserController();

        User user = User.builder().
                name("name")
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

        Map<Long, User> users = userController.getUsers();
        String name = user1.getName();

        assertEquals(2, users.size());
        assertEquals("login", name);

    }

    @Test
    void updateTest() {
        UserController userController = new UserController();

        User user = User.builder().
                name("name")
                .login("login")
                .birthday(LocalDate.of(1989, 10, 17))
                .email("email@yandex.ru").build();
        userController.create(user);

        User user1 = User.builder().
                name("new name")
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
        UserController userController = new UserController();

        User user = User.builder().
                name("name")
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

        List<User> sortedUsers = new ArrayList<>(users);
        sortedUsers.sort(Comparator.comparing(User::getName));

        assertEquals(sortedUsers, users);
    }
}

