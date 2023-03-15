package ru.yandex.practicum.filmorate.storage.film;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private HashMap<Integer, Film> filmStorage = new HashMap<Integer, Film>();

    private Integer counter = 0;

    public Film getFilm(int filmId){
        if(filmStorage.containsKey(filmId)){
        return filmStorage.get(filmId);
        }else {
            throw new FilmNotFoundExeption("Такой фильм не найден");
        }
    }

    public List<Film> getFilms(){
        return new ArrayList<Film>(filmStorage.values());
    }

    public Film postFilm(Film film) throws MyValidateExeption {
        validate(film);
        film.setId(++counter);
        filmStorage.put(film.getId(), film);
        return film;
    }

    public Film updateFilm(Film film) throws MyValidateExeption {
        validate(film);
        if(!filmStorage.containsKey(film.getId())){
            throw new FilmNotFoundExeption("Film or User with such id did not found");
        }
        filmStorage.put(film.getId(), film);
        return film;
    }


    @Override
    public void addLike(Integer targetFilmId, Integer userId) {
        Film film = filmStorage.get(targetFilmId);
        film.addLike(userId);
    }

    @Override
    public void removeLike(Integer targetFilmId, Integer userId) {
        Film film = filmStorage.get(targetFilmId);
        film.deleteLike(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        return  filmStorage.values().stream().sorted(Comparator.comparing(Film::getRate))
                .limit(count).collect(Collectors.toList());
    }

    @Override
    public Genre getGenre(int id) {
        return null;
    }

    @Override
    public List<Genre> getListOfGenres() {
        return null;
    }

    @Override
    public MPA getMPA(int id) {
        return null;
    }

    @Override
    public List<MPA> getListOfMPAs() {
        return null;
    }

    @Override
    public void removeFilm(int i) {
        filmStorage.remove(i);
    }



    void validate(Film item) throws MyValidateExeption{};


}

