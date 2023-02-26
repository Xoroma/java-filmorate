package ru.yandex.practicum.filmorate.model;

import lombok.*;
import ru.yandex.practicum.filmorate.exeptions.LikeAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeptions.LikeNotFoundExeption;

import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
public class Film extends AbstracItem{
    @NonNull
    private String name;

    @Size(min = 1, max = 200)
    private String description;

    @NotNull
    @Past
    private LocalDate releaseDate;

    @Positive
    private long duration;

    private List<Genre> genres; // у фильма может быть несколько жанров

    private MPA mpaRating;
    private int rate;

    private Set<Long> likes = new HashSet<>();



   public void  addLike(Long userId){
       if(!likes.add(userId)){
           throw new LikeAlreadyExistExeption("Вы уже поставили лайк фильму");
       }
       refreshRate();
   }


    public void  deleteLike(Long userId){
        if(!likes.remove(userId)){
            throw new LikeNotFoundExeption("При удалении лайка у фильма, лайк пользователя с таким ай ди не найден");
        }
        refreshRate();
    }


    private void refreshRate(){
       this.rate = likes.size();
    }
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class Genre {
       @Positive
       private  final int id;  // айди для таблиц
       @NotBlank
       private  final String name; // название жанра
    }
    @AllArgsConstructor
    @ToString
    @EqualsAndHashCode
    @Getter
    class MPA{
        @Positive
        private  final int id;  // айди для таблиц
        @NotBlank
        private  final String name; // название возрастного рейтинга
    }





}
