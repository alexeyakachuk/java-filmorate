package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest {


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
        assertEquals(name, "login");

    }
}
