package ru.yandex.practicum.filmorate.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.DuplicatedDataException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {
    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User newUser) {
        if (newUser.getEmail() == null || newUser.getEmail().isBlank()) {
            throw new ValidationException("Email должен быть указан");
        }

//        for (User user : users.values()) {
//            if (user.getEmail().equals(newUser.getEmail())) {
//                throw new DuplicatedDataException("Этот имейл уже используется");
//            }
//        }
        users.values().stream().filter(user -> user.getEmail().equals(newUser.getEmail())).forEach(user -> {
            throw new DuplicatedDataException("Этот имейл уже используется");
        });

        if (!newUser.getEmail().contains("@")) {
            throw new ValidationException("Имейл должен содержать символ @");
        }

        if (newUser.getLogin() == null || newUser.getLogin().isBlank()) {
            throw new ValidationException("Login должен быть указан");
        }

        if (newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        if (newUser.getBirthday() != null) {
            LocalDate birthDate = newUser.getBirthday();
            LocalDate today = LocalDate.now();
            if (birthDate.isAfter(today)) {
                throw new ValidationException("Дата рождения не может быть в будущем");
            }
        } else {
            throw new ValidationException("Дата рождения должна быть указана");
        }

        newUser.setId(getNextId());
        users.put(newUser.getId(), newUser);
        return newUser;
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
