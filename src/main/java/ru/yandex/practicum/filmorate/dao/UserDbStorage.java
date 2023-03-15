package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.sql.*;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Primary
@Repository("UserDAO")
@AllArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
       return jdbcTemplate.query("SELECT * FROM users",new UserMapper());
    }

    @Override
    public User getUser(Integer id) {
        User user;
        try {
            user = jdbcTemplate.queryForObject("SELECT * FROM users WHERE user_id = " + id, new UserMapper());
        } catch (DataAccessException e) {
            throw new UserNotFoundExeption(
                    String.format("Ошибка получения: пользователь с id=%d не найден.", id));
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT to_user FROM friendship WHERE from_user = ?", id);
        while (rowSet.next()) {
            if (user != null) {
                user.addFriend(rowSet.getInt("to_user"));

            } else throw new RuntimeException("Ошибка при получении пользователя.");
        }

        return user;
    }

    @Override
    public User postUser(User user) throws MyValidateExeption {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Number id;
        String sql = "INSERT INTO users (email, login, user_name, birthday) VALUES (?, ?, ?, ?)";

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);

        id = keyHolder.getKey();
        if (id != null) {
            return getUser(id.intValue());

        } else {
            throw new RuntimeException("Ошибка: пользователь не был добавлен.");
        }
    }

    @Override
    public User updateUser(User user) throws MyValidateExeption {
        int userId = user.getId();
        Integer responseUserId = getUserIdFromDB(userId);
        String sql = "UPDATE users SET email = ?, login = ?, user_name = ?, birthday = ? WHERE user_id = ?";
        if (responseUserId == null) {
            throw new UserNotFoundExeption(
                    String.format("Ошибка обновления в БД: пользователь с id=%d не найден.", userId));
        }

        jdbcTemplate.update(sql, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), userId);
        return getUser(user.getId());
    }

    @Override
    public void addFriend(Integer targetUserId, Integer friendId) {
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM friendship WHERE from_user = ? AND to_user = ?",
                targetUserId, friendId); // или наоборот??

        if (rowSet.next()) {
            throw new FriendAlreadyExistExeption(
                    String.format("Ошибка при добавлении друга для пользователя с id=%d: " +
                            "друг с id=%d уже добавлен.", targetUserId, friendId));
        }

        try {
            jdbcTemplate.update("INSERT INTO friendship (from_user, to_user)" +
                    "VALUES (?, ?)", targetUserId, friendId); // или наоборот?
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundExeption(String.format("Ошибка при добавлении друга с id=%d" +
                    " для пользователя с id=%d: один или оба пользователя не найдены.", friendId, targetUserId));
        }
    }

    @Override
    public void removeFriend(Integer targetUserId, Integer friendId) {
        Integer responseUserId = getUserIdFromDB(targetUserId);

        if (responseUserId == null) {
            throw new UserNotFoundExeption(
                    String.format("Ошибка при удалении друга с id=%d у пользователя с id=%d: " +
                            "пользователь не найден.", friendId, targetUserId));
        }

        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(
                "SELECT * FROM friendship WHERE from_user = ? AND to_user = ?",
                targetUserId, friendId);
        if (!rowSet.next()) {
            throw new FriendNotFoundExeption(
                    String.format("Ошибка при удалении друга у пользователя с id=%d: " +
                            "друг с id=%d не найден.", targetUserId, friendId));
        }
        jdbcTemplate.update("DELETE FROM friendship WHERE from_user = ? AND to_user = ?",
                targetUserId, friendId);
    }

    @Override
    public List<Integer> getAllFriends(User user) {
        return null;
    }

    @Override
    public List<User> getListOfFriends(Integer id) {
        User user = getUser(id);
        return List.copyOf(user.getFriendsIds().stream()
                .map(s->getUser(s))
                .collect(Collectors.toList()));
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = getUser(id);
        User otherUser = getUser(otherId);

        Set<Integer> listOfFriends =otherUser.getFriendsIds();
        return  List.copyOf(user.getFriendsIds().stream()
                .filter(listOfFriends::contains)
                .map(s->getUser(s))
                .collect(Collectors.toList()));
    }

    @Override
    public void removeUser(int userId) {
        Integer responseId = getUserIdFromDB(userId);

        if (responseId == null) {
            throw new UserNotFoundExeption(String.format("Ошибка удаления: юзер с id=%d не найден.", userId));
        }
        jdbcTemplate.update("DELETE FROM users WHERE user_id = ?", userId);
    }


    // Дополнительные методы и класс маппер.
    static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException, SQLException {
            return new User(
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getDate("birthday").toLocalDate()
            );
        }
    }

    private Integer getUserIdFromDB(int id) {
        Integer result;
        try {
            result = jdbcTemplate.queryForObject("SELECT user_id FROM users WHERE user_id = ?",
                    Integer.class, id);
        } catch (EmptyResultDataAccessException e) {
            result = null;
        }

        return result;
    }

}
