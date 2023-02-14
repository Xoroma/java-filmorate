package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import ru.yandex.practicum.filmorate.exeptions.LikeAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeptions.LikeNotFoundExeption;

import javax.validation.constraints.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
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



}
