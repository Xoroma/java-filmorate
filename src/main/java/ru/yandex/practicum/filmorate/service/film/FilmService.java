package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }


    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }


    public Film postFilm(Film film) throws MyValidateExeption {
        return filmStorage.postFilm(film);
    }

    public Film updateFilm(Film film) throws MyValidateExeption {
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(long filmId) {
        return filmStorage.getFilm(filmId);
    }

    public void addLike(Long filmId, Long userId) {
        filmStorage.addLike(filmId,userId);
    }


    public void deleteLike(Long filmId, Long userId) {
        filmStorage.removeLike(filmId,userId);
    }


    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getMostPopularFilms(count);
    }
}
