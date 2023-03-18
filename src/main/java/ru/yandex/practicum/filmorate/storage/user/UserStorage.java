package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> getUsers();

    public User getUser(Integer id);

    public User addUser(User item) throws MyValidateExeption;

    public User updateUser(User item) throws MyValidateExeption;

    public void addFriend(Integer targetUserId, Integer friendId);

    public void removeFriend(Integer targetUserId, Integer friendId);

    public List<Integer> getAllFriends(User user);

    public List<User> getListOfFriends(Integer id);

    public List<User> getCommonFriends(Integer id, Integer otherId);

    void removeUser(int userId);
}
