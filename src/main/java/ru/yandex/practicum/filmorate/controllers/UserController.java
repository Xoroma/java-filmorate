package ru.yandex.practicum.filmorate.controllers;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.SelfFriendsDeleteExeption;
import ru.yandex.practicum.filmorate.exeptions.WrongFriendIdExeption;
import ru.yandex.practicum.filmorate.exeptions.selfFriendsAddingExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.user.UserService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;


    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer userId) {
        return  userService.getUser(userId);
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getUsers();
    }


    @PostMapping
    public User postUser(@Valid @RequestBody User user) throws MyValidateExeption {
        validate(user);
        log.info("User {} was post to dataStorage",user);
        return  userService.addUser(user);
    }

    @PutMapping
    public User updateUser(@Valid @RequestBody User user) throws MyValidateExeption {
        validate(user);
        log.info("User {} was updated in dataStorage",user);
        return userService.updateUser(user);
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
        userService.addFriend(targetUserId,friendId);
   }
    @DeleteMapping("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable("id") Integer targetUserId, @PathVariable Integer friendId){
        if(targetUserId == friendId){
            throw new SelfFriendsDeleteExeption("It's imposible to  self delete");
        }
        log.info("User {} was deleted from friendList {} ",friendId,targetUserId);
        userService.removeFriend(targetUserId,friendId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId){
        userService.removeUser(userId);
        log.info("Пользователь удален");
    }


    @GetMapping("/{id}/friends")
    public List<User> getListOfFriends(@PathVariable Integer id){
        log.info("User {} was asked for get friendList ",id);
        return userService.getListOfFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId){
        log.info("User {} and {} was asked for get common friendList ",id, otherId);
       return userService.getCommonFriends(id,otherId);
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
