package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaDbStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public List<Mpa> findAll() {
        return mpaDbStorage.findAll();
    }

    public Mpa findById(int id) {
        try {
            return mpaDbStorage.findById(id);
        } catch (DataAccessException e) {
            throw new NotFoundException(e.getMessage());
        }


    }
}
