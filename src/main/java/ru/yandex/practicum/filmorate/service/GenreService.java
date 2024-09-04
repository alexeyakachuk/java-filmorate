package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.genre.GenreDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreService {
    private final GenreDbStorage genreDbStorage;

    public List<Genre> findAll() {

        return genreDbStorage.findAll();
    }

    public Genre findById(long id) {
        try {
            return genreDbStorage.findById(id);
        } catch (DataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }

    }
}