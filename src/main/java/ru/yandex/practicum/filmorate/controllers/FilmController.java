package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.AbstracItem;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController extends AbstractItemController<Film> {

    private static final LocalDate LIMIT_BIRTHDAY__OF_FILM = LocalDate.of(1895,12,28);
    @GetMapping
    @Override
    public List<Film> getItem() {

        return  super.getItem();
    }

    @PostMapping
    @Override
    public Film postItem(@Valid @RequestBody Film film) throws MyValidateExeption {
        log.info("Film {} was post to dataStorage",film);
        return super.postItem(film);
    }

    @PutMapping
    @Override
    public Film updateItem(@Valid @RequestBody Film film) throws MyValidateExeption {
        log.info("Film {} was updated to dataStorage",film);
        return super.updateItem(film);
    }


    @Override
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
