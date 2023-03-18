package ru.yandex.practicum.filmorate.service.film;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.List;

@Service
public class FilmService {
    private final FilmDbStorage filmStorage;

    public static final LocalDate LIMIT_BIRTHDAY_OF_FILM = LocalDate.of(1895,12,28);

    @Autowired
    public FilmService(FilmDbStorage filmStorage) {
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

    public Film getFilm(int filmId) {
        return filmStorage.getFilm(filmId);
    }

    public void addLike(Integer filmId, Integer userId) {
        filmStorage.addLike(filmId,userId);
    }


    public void deleteLike(Integer filmId, Integer userId) {
        filmStorage.removeLike(filmId,userId);
    }


    public List<Film> getMostPopularFilms(Integer count) {
        return filmStorage.getMostPopularFilms(count);
    }


    public void removeFilm(int filmId) {
        filmStorage.removeFilm(filmId);
    }


    public void removeLike(Integer filmId, Integer userId) {
        filmStorage.removeLike(filmId,userId);
    }


    public List<Genre> getListOfGenres() {
        return  filmStorage.getListOfGenres();
    }

    public Genre getGenre(int id) {
        return filmStorage.getGenre(id);
    }

    public List<MPA> getListOfMPAs() {
        return filmStorage.getListOfMPAs();
    }

    public MPA getMPA(int id) {
        return filmStorage.getMPA(id);
    }
}
