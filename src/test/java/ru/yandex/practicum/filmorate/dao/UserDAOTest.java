package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exeptions.FriendNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundExeption;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AllArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:testSchema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class UserDAOTest {

    UserDbStorage storage;

    @Test
    public void postUserTest() throws MyValidateExeption {
        User user = makeDefaultUser();
        storage.postUser(user);
        user.setId(1);

        assertEquals(user, storage.getUser(1));
    }

    @Test
    public void shouldThrowExceptionForGettingNonExistentUser() {
        assertThrows(UserNotFoundExeption.class, () -> storage.getUser(1));
    }

    @Test
    public void getUsersTest() throws MyValidateExeption {
        User user1 = makeDefaultUser();
        storage.postUser(user1);
        user1.setId(1);

        User user2 = makeDefaultUser();
        storage.postUser(user2);
        user2.setId(2);

        assertEquals(List.of(user1, user2), storage.getUsers());
    }

    @Test
    public void addFriendTest() throws MyValidateExeption {
        User user1 = makeDefaultUser();
        storage.postUser(user1);

        User user2 = makeDefaultUser();
        storage.postUser(user2);

        storage.addFriend(1, 2);
        assertTrue(storage.getUser(1).getFriendsIds().contains(2));
        assertTrue(storage.getUser(2).getFriendsIds().isEmpty());

        storage.addFriend(2, 1);
        assertTrue(storage.getUser(2).getFriendsIds().contains(1));
    }

    @Test
    public void shouldThrowExceptionsForAddingNonExistentUsersAndFriends() throws MyValidateExeption {
        assertThrows(UserNotFoundExeption.class, () -> storage.addFriend(1, 2));

        User user = makeDefaultUser();
        storage.postUser(user);

        assertThrows(UserNotFoundExeption.class, () -> storage.addFriend(1, 2));
    }

    @Test
    public void updateUserTest() throws MyValidateExeption {
        User user = makeDefaultUser();
        storage.postUser(user);

        user.setName("New name");
        user.setId(1);
        storage.updateUser(user);

        assertEquals(user, storage.getUser(1));
    }

    @Test
    public void shouldThrowExceptionForUpdatingNonExistentUser() {
        User user = makeDefaultUser();
        assertThrows(UserNotFoundExeption.class, () -> storage.updateUser(user));
    }

    @Test
    public void removeFriendTest() throws MyValidateExeption {
        User user1 = makeDefaultUser();
        storage.postUser(user1);

        User user2 = makeDefaultUser();
        storage.postUser(user2);

        storage.addFriend(1, 2);
        assertTrue(storage.getUser(1).getFriendsIds().contains(2));

        storage.removeFriend(1, 2);
        assertTrue(storage.getUser(1).getFriendsIds().isEmpty());
    }

    @Test
    public void shouldThrowExceptionForRemovingNonExistentFriendOrFromAbsentUser() throws MyValidateExeption {
        assertThrows(UserNotFoundExeption.class, () -> storage.removeFriend(1, 2));

        User user = makeDefaultUser();
        storage.postUser(user);

        assertThrows(FriendNotFoundExeption.class, () -> storage.removeFriend(1, 2));
        assertThrows(UserNotFoundExeption.class, () -> storage.removeFriend(3, 1));
    }

    @Test
    public void removeUser() throws MyValidateExeption {
        User user = makeDefaultUser();
        storage.postUser(user);
        user.setId(1);
        storage.removeUser(1);
        assertThrows(UserNotFoundExeption.class, () -> storage.getUser(1));
    }

    private User makeDefaultUser() {
        return new User(
                1,
                "TestName",
                "test@test.ru",
                "",
                LocalDate.parse("1988-04-12"));
    }
}