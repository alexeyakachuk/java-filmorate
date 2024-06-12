package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
@Getter
public class UserController {


    private final Map<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        List<User> users = new ArrayList<>(this.users.values());
        return users;
    }

    // создание
    @PostMapping
    public User create(@Valid @RequestBody User newUser) {

        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        long nextId = getNextId();
        newUser.setId(nextId);
        users.put(newUser.getId(), newUser);
        log.info("Создан пользователь с id {}", nextId);
        return newUser;
    }

    @PutMapping
    public User update(@Valid @RequestBody User newUser) {
        checkId(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Пользователь c id {} обновлен", newUser.getId());
        return newUser;
    }

    private void checkId(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new ValidationException("Пользователя с таким id нет");
        }

    }

    private boolean isEmailUnique(User newUser) {
        Optional<User> any = users.values()
                .stream()
                .filter(user -> !user.getId().equals(newUser.getId()) && user.getEmail().equals(newUser.getEmail()))
                .findAny();
        return any.isEmpty();
    }

    private long getNextId() {
        if (users.isEmpty()) {
            return 1;
        }
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }


}
