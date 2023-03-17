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

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@Slf4j
@AllArgsConstructor
public class FilmController{


    private final FilmService filmService;


    @GetMapping("/films/{id}")
    public Film getFilm(@PathVariable(value = "id") int filmId) {

        return  filmService.getFilm(filmId);
    }

    @GetMapping("/films")
    public List<Film> getFilms() {
        return  filmService.getFilms();
    }

    @PostMapping("/films")
    public Film postFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was post to dataStorage",film);
        return filmService.postFilm(film);
    }

    @PutMapping("/films")
    public Film updateFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was updated to dataStorage",film);
        return filmService.updateFilm(film);
    }

    @DeleteMapping("/films/{filmId}")
    public void removeFilm(@PathVariable int filmId){
        filmService.removeFilm(filmId);
        log.info("Фильм с id = {} удален", filmId);
    }

    // методы связанные с лайками(не собаками)
    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId){
        filmService.addLike(filmId,userId);
        log.info("Like {} was add to film",filmId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId){
        filmService.removeLike(filmId,userId);
        log.info("Like {} was delete from film",filmId);
    }

    @GetMapping("/films/popular")
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count){
        log.info("Most popular films were returned");
        return filmService.getMostPopularFilms(count);
    }

    @GetMapping("/genres")
    public List<Genre> getListOfGenres(){
        return  filmService.getListOfGenres();
    }

    @GetMapping("/genres/{id}")
    public Genre getgenre(@PathVariable int id){
        return filmService.getGenre(id);
    }

    @GetMapping("/mpa")
    public List<MPA> getListOfMpas(){
        return  filmService.getListOfMPAs();
    }

    @GetMapping("/mpa/{id}")
    public MPA getMPA(@PathVariable int id){
        return filmService.getMPA(id);
    }




    void validate(Film film) throws MyValidateExeption {
        if(film.getReleaseDate().isBefore(FilmService.LIMIT_BIRTHDAY_OF_FILM)){
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
