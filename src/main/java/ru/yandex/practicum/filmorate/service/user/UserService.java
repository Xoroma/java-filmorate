package ru.yandex.practicum.filmorate.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public void addFriend(Long targetUserId, Long friendId){
        userStorage.addFriend(targetUserId, friendId);
    }

    public void removeFriend(Long targetUserId, Long friendId){
       userStorage.removeFriend(targetUserId,friendId);
    }

    public List<Long> getAllFriends(User user){
       return userStorage.getAllFriends(user);
    }

    public List<User> getUsers() {
       return userStorage.getUsers();
    }


    public User postUser(User user) throws MyValidateExeption {
        return userStorage.postUser(user);
    }


    public User updateUser(User user) throws MyValidateExeption {
        return userStorage.updateUser(user);
    }

    public User getUser(long userId) {
        return userStorage.getUser(userId);
    }

    public List<User> getListOfFriends(Long id) {
        return userStorage.getListOfFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}
