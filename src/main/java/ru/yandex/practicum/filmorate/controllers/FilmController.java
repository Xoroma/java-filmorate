package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController{
    private FilmStorage filmStorage;

    private FilmService filmService;
    private static final LocalDate LIMIT_BIRTHDAY__OF_FILM = LocalDate.of(1895,12,28);

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable(value = "id") long filmId) {

        return  filmService.getFilm(filmId);
    }

    @GetMapping
    public List<Film> getFilms() {
        return  filmService.getFilms();
    }

    @PostMapping
    public Film postFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was post to dataStorage",film);
        return filmService.postFilm(film);
    }

    @PutMapping
    public Film updateFilm(@Valid @RequestBody Film film) throws MyValidateExeption {
        validate(film);
        log.info("Film {} was updated to dataStorage",film);
        return filmService.updateFilm(film);
    }

    // методы связанные с лайками(не собаками)
    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId){
        filmService.addLike(filmId,userId);
        log.info("Like {} was add to film",filmId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable("id") Long filmId, @PathVariable("userId") Long userId){
        filmService.deleteLike(filmId,userId);
        log.info("Like {} was delete from film",filmId);
    }

    @GetMapping("/popular")
    public List<Film> getMostPopularFilms(@RequestParam(name = "count", defaultValue = "10") Integer count){
        log.info("Most popular films were returned");
        return filmService.getMostPopularFilms(count);

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
