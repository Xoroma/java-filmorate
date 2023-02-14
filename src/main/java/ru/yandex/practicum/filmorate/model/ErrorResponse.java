package ru.yandex.practicum.filmorate.model;

public class ErrorResponse {
    // название ошибки
    String error;
    // подробное описание
    String description;

    public ErrorResponse(String error) {
        this.error = error;

    }

    // геттеры необходимы, чтобы Spring Boot мог получить значения полей
    public String getError() {
        return error;
    }

    public String getDescription() {
        return description;
    }
}