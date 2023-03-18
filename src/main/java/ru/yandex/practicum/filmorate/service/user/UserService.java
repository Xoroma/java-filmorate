package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.UserDbStorage;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.User;


import java.util.List;

@Service
public class UserService {
    private final UserDbStorage userStorage;

    @Autowired
    public UserService(UserDbStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(Integer targetUserId, Integer friendId){
        userStorage.addFriend(targetUserId, friendId);
    }

    public void removeFriend(Integer targetUserId, Integer friendId){
       userStorage.removeFriend(targetUserId,friendId);
    }

    public List<Integer> getAllFriends(User user){
       return userStorage.getAllFriends(user);
    }

    public List<User> getUsers() {
       return userStorage.getUsers();
    }


    public User addUser(User user) throws MyValidateExeption {
        return userStorage.addUser(user);
    }


    public User updateUser(User user) throws MyValidateExeption {
        return userStorage.updateUser(user);
    }

    public User getUser(Integer userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getListOfFriends(Integer id) {
        return userStorage.getListOfFriends(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }

    public void removeUser(int userId) {
        userStorage.removeUser(userId);
    }

}
