package ru.yandex.practicum.filmorate.exeptions;

public class WrongFriendIdExeption extends RuntimeException{
    public WrongFriendIdExeption(String message) {
        super(message);
    }
}
