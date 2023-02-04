package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;
@Data
@NoArgsConstructor
public class Film extends AbstracItem{
    @NonNull
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    private LocalDate releaseDate;

    @Positive
    private long duration;
}
