package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.yandex.practicum.filmorate.exeptions.LikeAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeptions.LikeNotFoundExeption;

import javax.validation.constraints.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Film{

    private int id;
    @NonNull
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    @Past
    private LocalDate releaseDate;

    @Positive
    private int duration;

    private List<Genre> genres = new ArrayList<>(); // у фильма может быть несколько жанров

    private MPA mpa;
    private int rate;
    @JsonIgnore
    private Set<Integer> likes = new HashSet<>();


    public Film(int id, @NonNull String name, String description, LocalDate releaseDate, int duration, MPA mpa, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.rate = rate;
    }


    public void  addLike(Integer userId){
       if(!likes.add(userId)){
           throw new LikeAlreadyExistExeption("Вы уже поставили лайк фильму");
       }
       refreshRate();
   }


    public void  deleteLike(Integer userId){
        if(!likes.remove(userId)){
            throw new LikeNotFoundExeption("При удалении лайка у фильма, лайк пользователя с таким ай ди не найден");
        }
        refreshRate();
    }

   // Жанр
    private void refreshRate(){
       this.rate = likes.size();
    }

    public void addGenre(Genre genre){
        genres.add(genre);
    }

    // МПА Рейтинг






}
