package ru.yandex.practicum.filmorate.exeptions;

public class FilmNotFoundExeption extends RuntimeException{

    public FilmNotFoundExeption(String message) {
        super(message);
    }
}
