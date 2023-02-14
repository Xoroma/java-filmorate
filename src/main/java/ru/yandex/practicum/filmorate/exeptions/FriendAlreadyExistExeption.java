package ru.yandex.practicum.filmorate.exeptions;

public class FriendAlreadyExistExeption extends RuntimeException{
    public FriendAlreadyExistExeption(String message) {
        super(message);
    }
}
