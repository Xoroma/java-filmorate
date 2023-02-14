package ru.yandex.practicum.filmorate.exeptions;

public class LikeAlreadyExistExeption extends RuntimeException{

    public LikeAlreadyExistExeption(String message) {
        super(message);
    }
}
