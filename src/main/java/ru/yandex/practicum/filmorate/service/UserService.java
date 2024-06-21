package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage storage) {
        this.userStorage = storage;
    }

    public List<User> findAllUsers() {
        return userStorage.findAllUsers();
    }

    public User findUser(long id) {
        return userStorage.findUser(id);
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {
        return userStorage.updateUser(newUser);
    }

    public void addToFriendsList(long userId, long newFriendId) {
        User user = findUser(userId);
        User newFriend = findUser(newFriendId);
        Set<Long> userFriendList = user.getFriends();
        Set<Long> newFriendFriendList = newFriend.getFriends();
        userFriendList.add(newFriendId);
        newFriendFriendList.add(userId);
//        user.setFriends(newFriendFriendList);
//        newFriend.setFriends(userFriendList);
        updateUser(user);
        updateUser(newFriend);
        log.info("Пользователи {} и {} теперь друзья", user.getName(), newFriend.getName());
    }
}
