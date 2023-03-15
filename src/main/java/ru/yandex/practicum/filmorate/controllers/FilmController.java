package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
//@RequestMapping("/films")
@Slf4j
@AllArgsConstructor
public class FilmController{
    private final FilmDbStorage filmDbStorage;

//    private FilmService filmService;


    private static final LocalDate LIMIT_BIRTHDAY__OF_FILM = LocalDate.of(1895,12,28);

//    @Autowired
//    public FilmController(FilmService filmService) {
//        this.filmService = filmService;
//    }

    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable(value = "id") int filmId) {

        return  filmDbStorage.getFilm(filmId);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return  filmDbStorage.getFilms();
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was post to dataStorage",film);
        return filmDbStorage.postFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was updated to dataStorage",film);
        return filmDbStorage.updateFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public void removeFilm(@PathVariable int filmId){
        filmDbStorage.removeFilm(filmId);
        log.info("Фильм с id = {} удален", filmId);
    }

    // методы связанные с лайками(не собаками)
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId){
        filmDbStorage.addLike(filmId,userId);
        log.info("Like {} was add to film",filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId){
        filmDbStorage.removeLike(filmId,userId);
        log.info("Like {} was delete from film",filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count){
        log.info("Most popular films were returned");
        return filmDbStorage.getMostPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getListOfGenres(){
        return  filmDbStorage.getListOfGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getgenre(@PathVariable int id){
        return filmDbStorage.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getListOfMpas(){
        return  filmDbStorage.getListOfMPAs();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable int id){
        return filmDbStorage.getMPA(id);
    }




    void validate(Film film) throws MyValidateExeption {
        if(film.getReleaseDate().isBefore(LIMIT_BIRTHDAY__OF_FILM)){
            throw new MyValidateExeption("Film release date must be older than 28.12.1895");
        }

        if(film.getName()==null || film.getName().isBlank()){
            throw new MyValidateExeption("User name can't be empty");
        }

        if(film.getDescription().length() > 200){
            throw new MyValidateExeption("Description of the film can't be more than 200");
        }

        if(film.getDuration() < 0){
            throw new MyValidateExeption("Lenght of the film must be positive");
        }


    }
}
