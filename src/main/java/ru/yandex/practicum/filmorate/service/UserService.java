package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        List<User> allUsers = userStorage.findAllUsers();
        Map<Long, Set<Long>> friendsByUserId = userStorage.getAllFriends();

        for (User user : allUsers) {
            Set<Long> friendsId = friendsByUserId.getOrDefault(user.getId(), new HashSet<>());
            user.setFriends(friendsId);
        }
        return allUsers;

//        return allUsers.stream()
//                .peek(user -> user.setFriends(friendsByUserId.getOrDefault(user.getId(), new HashSet<>())))
//                .collect(Collectors.toList());
    }

    public User findUser(long id) {

        User user = userStorage.findUser(id);
        if (user == null) {
            throw new NotFoundException("Пользователь с id " + id + " не найден"); // Добавьте это условие
        }
        List<Long> friendsId = userStorage.getFriendsByUserId(id);
        user.setFriends(new HashSet<>(friendsId));
        return user;
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User newUser) {

        User user = userStorage.updateUser(newUser);

        return user;
    }

    public void addToFriendsList(long userId, long newFriendId) {

        User user = findUser(userId);
        User newFriend = findUser(newFriendId);

        userStorage.addFriend(userId, newFriendId);

        log.info("Пользователь {} добавил {} в друзья", user.getName(), newFriend.getName());
    }

    public List<User> getFriendsUser(long id) {
        findUser(id);

        return userStorage.getUserFriends(id);
    }

    public void deleteFromFriendList(long userId, long friendId) {
        User user = findUser(userId);
        User friendUser = findUser(friendId);

        userStorage.deleteFriend(userId, friendId);

        log.info("Пользователь {} удалил из друзей {}", user.getName(), friendUser.getName());
    }

    public List<User> showMutualFriends(long userId, long friendId) {
        List<Long> friends = userStorage.getFriendsByUserId(userId);
        List<Long> otherFriends = userStorage.getFriendsByUserId(friendId);

        return friends.stream()
                .filter(otherFriends::contains)
                .map(userStorage::findUser)
                .collect(Collectors.toList());
    }
}
