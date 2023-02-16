package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import org.apache.coyote.http11.filters.SavedRequestInputFilter;
import ru.yandex.practicum.filmorate.exeptions.FriendAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeptions.FriendNotFoundExeption;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @Past
    private LocalDate birthday;

    private Set<Long> friendsIds = new HashSet<>(); //список друзей пользователя по айдишникам.

    public void addFriend(Long friendId){
        if(!friendsIds.add(friendId)){
            throw new FriendAlreadyExistExeption("Друг с таким ай ди уже добавлен");
        }
    }

    public void deleteFriend(Long friendId){
        if(!friendsIds.remove(friendId)){
            throw new FriendNotFoundExeption("Друг с таким ай ди не найден");
        }
    }

}
