package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.apache.coyote.http11.filters.SavedRequestInputFilter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AbstracItem{

    private String name;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotNull
    @PastOrPresent
    private LocalDate birthday;

}
