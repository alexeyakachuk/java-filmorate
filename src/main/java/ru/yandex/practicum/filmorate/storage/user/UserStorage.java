package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    List<User> findAllUsers();

    User findUser(long id);

    User createUser(User newUser);

    User updateUser(User newUser);

    void addFriend(long fromUserId, long toUserId);
}
