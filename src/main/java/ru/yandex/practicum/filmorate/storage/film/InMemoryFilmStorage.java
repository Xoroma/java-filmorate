package ru.yandex.practicum.filmorate.storage.film;


import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InMemoryFilmStorage implements FilmStorage{
    private HashMap<Long, Film> filmStorage = new HashMap<Long, Film>();

    private Long counter = 0l;

    public Film getFilm(long filmId){
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
    public void addLike(Long targetFilmId, Long userId) {
        Film film = filmStorage.get(targetFilmId);
        film.addLike(userId);
    }

    @Override
    public void removeLike(Long targetFilmId, Long userId) {
        Film film = filmStorage.get(targetFilmId);
        film.deleteLike(userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
//       return  filmStorage.values().stream().sorted((p0,p1)->{
//           int comp = compare(p0,p1);
//           return comp;
//       }).limit(10).collect(Collectors.toList());

        return  filmStorage.values().stream().sorted(Comparator.comparing(Film::getRate))
                .limit(count).collect(Collectors.toList());
    }

//    private int compare(Film p0, Film p1) {
//       //прямой порядок сортировки
//        return Integer.compare(p0.getRate(), p1.getRate());
//
//    }





    void validate(Film item) throws MyValidateExeption{};


}

