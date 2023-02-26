package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {

    public List<Film> getFilms();

    public Film postFilm(Film film) throws MyValidateExeption;

    public Film updateFilm(Film film) throws MyValidateExeption;


    public void addLike(Long targetFilmId, Long userId);


    public void removeLike(Long targetFilmId, Long userId);

    public List<Film> getMostPopularFilms(Integer count);


    Film getFilm(long filmId);
}
