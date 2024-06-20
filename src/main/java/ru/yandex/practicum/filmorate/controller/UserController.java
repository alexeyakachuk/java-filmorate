package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@Getter
public class UserController {

    InMemoryUserStorage inMemoryUserStorage = new InMemoryUserStorage();

    @GetMapping
    public List<User> findAll() {

        return inMemoryUserStorage.findAllUsers();
    }

    // создание
    @PostMapping
    public User create(@Valid @RequestBody User newUser) {
        return inMemoryUserStorage.createUser(newUser);
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        return inMemoryUserStorage.updateUser(newUser);
    }
}
