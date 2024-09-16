package ru.yandex.practicum.filmorate.storage.likes;

import java.util.List;

public interface LikeStorage {

    List<Long> findByIdUserLikes(long userId);
}
