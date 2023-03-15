package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Getter
public class MPA{
    @Positive
    private  final int id;  // айди для таблиц
    @NotBlank
    private  final String name; // название возрастного рейтинга
}