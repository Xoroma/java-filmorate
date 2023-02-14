package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    public List<User> getUsers();

    public User getUser(Long id);

    public User postUser(User item) throws MyValidateExeption;

    public User updateUser(User item) throws MyValidateExeption;

    public void addFriend(Long targetUserId, Long friendId);

    public void removeFriend(Long targetUserId, Long friendId);

    public List<Long> getAllFriends(User user);

    public List<User> getListOfFriends(Long id);

    public List<User> getCommonFriends(Long id, Long otherId);
}
