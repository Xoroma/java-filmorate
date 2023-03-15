package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.data.relational.core.sql.In;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage{
    private HashMap<Integer, User> userStorage = new HashMap();

    private Integer counter = 0;


    @Override
    public User getUser(Integer id) {
        if(userStorage.containsKey(id)){
            return userStorage.get(id);
        }else {
            throw new UserNotFoundExeption(String.format("Пользователь с %d не найден", id));
        }
    }

    @Override
    public List<User> getUsers(){
        return new ArrayList<User>(userStorage.values());
    }

    @Override
    public User postUser(User user) throws MyValidateExeption {
        validate(user);
        user.setId(++counter);
        userStorage.put(user.getId(), user);
        return user;
    }
    @Override
    public User updateUser(User user) throws MyValidateExeption {
        validate(user);
        if(!userStorage.containsKey(user.getId())){
            throw new UserNotFoundExeption("Film or User with such id did not found");
        }
        userStorage.put(user.getId(), user);
        return user;
    }



    @Override
    public void addFriend(Integer targetUserId, Integer friendId) {
        User user = getUser(targetUserId);
        user.addFriend(friendId);
        // автоматический аппрув в друзья,
        User friendUser = userStorage.get(friendId);
        friendUser.addFriend(targetUserId);

    }

    @Override
    public void removeFriend(Integer targetUserId, Integer friendId) {
        User user = getUser(targetUserId);
        user.deleteFriend(friendId);
    }

    @Override
    public List<Integer> getAllFriends(User user) {
        Integer id = user.getId();
        return (List<Integer>) user.getFriendsIds();
    }

    public List<User> getListOfFriends(Integer id){
        User user = userStorage.get(id);
        return List.copyOf(user.getFriendsIds().stream()
                .map(s->userStorage.get(s))
                .collect(Collectors.toList()));
    }

   public List<User> getCommonFriends(Integer id, Integer otherId){
        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);

        Set<Integer> listOfFriends =otherUser.getFriendsIds();
        return  List.copyOf(user.getFriendsIds().stream()
                .filter(listOfFriends::contains)
                .map(s->userStorage.get(s))
                .collect(Collectors.toList()));
    }

    @Override
    public void removeUser(int userId) {
        userStorage.remove(userId);
    }

    void validate(User user) throws MyValidateExeption{};

}
