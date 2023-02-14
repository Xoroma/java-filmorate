package ru.yandex.practicum.filmorate.exeptions;

public class LikeNotFoundExeption extends RuntimeException{
    public LikeNotFoundExeption(String message) {
        super(message);
    }
}
