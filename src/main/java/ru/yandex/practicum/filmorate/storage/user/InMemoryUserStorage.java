package ru.yandex.practicum.filmorate.storage.user;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;

@Slf4j
@Component
@Getter
public class InMemoryUserStorage implements UserStorage {

    private final Map<Long, User> users = new HashMap<>();


    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>(this.users.values());
        return users;
    }

    @Override
    public User findUser(long id) {
        System.out.println(findAllUsers());
        if (!users.containsKey(id)) {
            throw new NotFoundException("Пользователя с таким id " + id + " нет");

        }
        return users.get(id);
    }

    @Override
    public User createUser(User newUser) {
        if (newUser.getName() == null || newUser.getName().isBlank()) {
            newUser.setName(newUser.getLogin());
        }

        newUser.setFriends(new HashSet<>());

        long nextId = getNextId();
        newUser.setId(nextId);
        users.put(newUser.getId(), newUser);
        log.info("Создан пользователь с id {}", nextId);
        return newUser;
    }

    @Override
    public User updateUser(@Valid @RequestBody User newUser) {
        checkId(newUser);
        Long id = newUser.getId();
        User oldUser = users.get(id);
        updateFields(oldUser, newUser);
        users.put(id, oldUser);
        log.info("Пользователь c id {} обновлен", id);
        return oldUser;
    }

    private void updateFields(User oldUser, User newUser) {
        if (newUser.getBirthday() != null) {
            oldUser.setBirthday(newUser.getBirthday());
        }
        if (newUser.getName() != null) {
            oldUser.setName(newUser.getName());
        }
        if (newUser.getEmail() != null) {
            oldUser.setEmail(newUser.getEmail());
        }
        if (newUser.getLogin() != null) {
            oldUser.setLogin(newUser.getLogin());
        }
        if (newUser.getFriends() != null) {
            oldUser.setFriends(newUser.getFriends());
        }
    }

    private void checkId(User newUser) {
        if (newUser.getId() == null) {
            throw new ValidationException("Id пользователя должен быть указан");
        }
        if (!users.containsKey(newUser.getId())) {
            throw new NotFoundException("Пользователя с таким id " + newUser.getId() + " нет");
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
}
