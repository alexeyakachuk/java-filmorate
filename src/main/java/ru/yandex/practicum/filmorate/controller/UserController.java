package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
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
        users.sort(Comparator.comparing(User::getName));
        return users;
    }

    // создание
    @PostMapping
    public User create(@RequestBody User newUser) {
        validate(newUser);

        if (newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        long nextId = getNextId();
        newUser.setId(nextId);
        users.put(newUser.getId(), newUser);
        log.info("Создан пользователь с id {}", nextId);
        return newUser;
    }

    @PutMapping
    public User update(@RequestBody User newUser) {
        checkId(newUser);
        validate(newUser);
        users.put(newUser.getId(), newUser);
        log.info("Пользователь c id {} обновлен", newUser.getId());
        return newUser;
    }

    private void checkId(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
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
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private void validate(User newUser) {
        try {
            if (ObjectUtils.isEmpty(newUser.getEmail())) {
                throw new ValidationException("Email должен быть указан");
            }

            if (!isEmailUnique(newUser)) {
                throw new ValidationException("Этот Email уже используется");
            }

            if (!newUser.getEmail().contains("@")) {
                throw new ValidationException("Имейл должен содержать символ @");
            }

            if (ObjectUtils.isEmpty(newUser.getLogin())) {
                throw new ValidationException("Login должен быть указан");
            }

            if (newUser.getLogin().contains(" ")) {
                throw new ValidationException("Login не может содержать пробелы");
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
        }catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
    }
}
