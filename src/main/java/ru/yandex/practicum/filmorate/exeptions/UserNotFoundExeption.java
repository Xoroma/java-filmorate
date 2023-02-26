package ru.yandex.practicum.filmorate.exeptions;

public class UserNotFoundExeption extends RuntimeException{
    public UserNotFoundExeption(String message) {
        super(message);
    }
}
