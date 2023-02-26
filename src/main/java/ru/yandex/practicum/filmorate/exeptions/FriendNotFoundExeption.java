package ru.yandex.practicum.filmorate.exeptions;

public class FriendNotFoundExeption extends RuntimeException{

    public FriendNotFoundExeption(String message) {
        super(message);
    }
}
