package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film getFilm(int filmId);

    public List<Film> getFilms();

    public Film postFilm(Film film) throws MyValidateExeption;

    public Film updateFilm(Film film) throws MyValidateExeption;


    public void addLike(Integer targetFilmId, Integer userId);



    public void removeLike(Integer targetFilmId, Integer userId);

    public List<Film> getMostPopularFilms(Integer count);

    Genre getGenre(int id);

    List<Genre> getListOfGenres();

    MPA getMPA(int id);

    List<MPA> getListOfMPAs();

    void removeFilm(int i);
}
