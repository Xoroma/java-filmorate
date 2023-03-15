package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.SelfFriendsDeleteExeption;
import ru.yandex.practicum.filmorate.exeptions.WrongFriendIdExeption;
import ru.yandex.practicum.filmorate.exeptions.selfFriendsAddingExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
//    private final UserService userService;
//
//    @Autowired
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
    @Autowired
    private final UserDbStorage userDbStorage;

    @Autowired
    public UserController(UserDbStorage userDbStorage) {
        this.userDbStorage = userDbStorage;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return  userDbStorage.getUser(userId);
    }

    @GetMapping
    public List<User> getUsers(){
        return userDbStorage.getUsers();
    }


    @PostMapping
    public User postUser(@Valid @RequestBody User user) throws MyValidateExeption {
        validate(user);
        log.info("User {} was post to dataStorage",user);
        return  userDbStorage.postUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws MyValidateExeption {
        validate(user);
        log.info("User {} was updated in dataStorage",user);
        return userDbStorage.updateUser(user);
    }

    //Методы добавления в друзья,удаления, получение списка общих друзей.
   @PutMapping("/{id}/friends/{friendId}")
   public void addFriend(@PathVariable("id") Integer targetUserId, @PathVariable Integer friendId){
        if(targetUserId == friendId){
            throw new selfFriendsAddingExeption("It's imposible to  self adding");
        }
        if(friendId < 0){
            throw  new WrongFriendIdExeption("Friend id must be positive");
        }
       log.info("User {} was added to friendList {} ",friendId,targetUserId);
        userDbStorage.addFriend(targetUserId,friendId);
   }
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer targetUserId, @PathVariable Integer friendId){
        if(targetUserId == friendId){
            throw new SelfFriendsDeleteExeption("It's imposible to  self delete");
        }
        log.info("User {} was deleted from friendList {} ",friendId,targetUserId);
        userDbStorage.removeFriend(targetUserId,friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId){
        userDbStorage.removeUser(userId);
        log.info("Пользователь удален");
    }


    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id){
        log.info("User {} was asked for get friendList {} ",id);
        return userDbStorage.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId){
        log.info("User {} and {} was asked for get common friendList ",id, otherId);
       return userDbStorage.getCommonFriends(id,otherId);
    }

    void validate(User user) throws MyValidateExeption {
        if(user.getName()==null || user.getName().isBlank()){
            log.info("Empty user name. Name had set as a login");
            user.setName(user.getLogin());
        }

        if(user.getEmail().isBlank() || !user.getEmail().contains("@")){
            throw new MyValidateExeption("Email can't be empty");
        }

        if(user.getLogin()==null || user.getLogin().isEmpty()||user.getLogin().contains(" ")){
            throw new MyValidateExeption("Login can't be empty");
        }

        if(user.getBirthday().isAfter(LocalDate.now())){
            throw new MyValidateExeption("Birthdate can't be in the future");
        }

    }


}
