package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface UserStorage {

    List<User> findAllUsers();

    Map<Long, Set<Long>> getAllFriends();

    User findUser(long id);

    User createUser(User newUser);

    User updateUser(User newUser);

    void addFriend(long fromUserId, long toUserId);

    List<Long> getFriendsByUserId(long userId);

    void deleteFriend(long userId, long friendId);

    List<User> getUserFriends(long userId);
}

