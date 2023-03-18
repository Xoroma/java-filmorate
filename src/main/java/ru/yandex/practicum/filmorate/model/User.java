package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import ru.yandex.practicum.filmorate.exeptions.FriendAlreadyExistExeption;
import ru.yandex.practicum.filmorate.exeptions.FriendNotFoundExeption;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User{
    private int id;
    private String name;
    @Email
    private String email;
    @NotBlank
    private String login;
    @NotNull
    @Past
    private LocalDate birthday;

    @JsonIgnore
    private Set<Integer> friendsIds = new HashSet<>(); //список друзей пользователя по айдишникам.

    public User(int id, String name, String email, String login, LocalDate birthday) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }


    public void addFriend(Integer friendId){
        if(!friendsIds.add(friendId)){
            throw new FriendAlreadyExistExeption("Друг с таким ай ди уже добавлен");
        }
    }

    public void deleteFriend(Integer friendId){
        if(!friendsIds.remove(friendId)){
            throw new FriendNotFoundExeption("Друг с таким ай ди не найден");
        }
    }


}
