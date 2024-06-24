package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
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
        updateUser(user);
        updateUser(newFriend);
        log.info("Пользователи {} и {} теперь друзья", user.getName(), newFriend.getName());
    }

    public List<User> getFriendsUser(long id) {
        User user = findUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователя с id " + id + " нет");
        }
        return findAllUsers().stream()
                .filter(u -> u.getFriends().contains(id))
                .collect(Collectors.toList());
    }

    public void deleteFromFriendList(long userId, long friendId) {
        User user = findUser(userId);
        User friendUser = findUser(friendId);

        user.getFriends().remove(friendId);
        friendUser.getFriends().remove(userId);
        updateUser(user);
        updateUser(friendUser);
        log.info("Пользователь {} удалил из друзей {}", user.getName(), friendUser.getName());
    }

    public List<User> showMutualFriends(long userId, long friendId) {
        User user = findUser(userId);
        User friendUser = findUser(friendId);
        List<Long> friendsId = user.getFriends().stream()
                .filter(friendUser.getFriends()::contains)
                .toList();
        List<User> allUsers = userStorage.findAllUsers();
        List<User> mutualFriends = new ArrayList<>();

        for (User user1 : allUsers) {
            if (friendsId.contains(user1.getId())) {
                mutualFriends.add(user1);
            }
        }
        return mutualFriends;
    }
}
