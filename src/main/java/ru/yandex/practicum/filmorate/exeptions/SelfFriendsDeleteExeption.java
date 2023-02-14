package ru.yandex.practicum.filmorate.exeptions;

public class SelfFriendsDeleteExeption extends RuntimeException{
    public SelfFriendsDeleteExeption(String message) {
        super(message);
    }
}
