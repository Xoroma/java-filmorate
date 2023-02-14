package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.film.FilmService;
import ru.yandex.practicum.filmorate.service.user.UserService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));
	UserController userController = new UserController(new UserService(new UserStorage() {
		@Override
		public List<User> getUsers() {
			return null;
		}

		@Override
		public User getUser(Long id) {
			return null;
		}

		@Override
		public User postUser(User item) throws MyValidateExeption {
			return null;
		}

		@Override
		public User updateUser(User item) throws MyValidateExeption {
			return null;
		}

		@Override
		public void addFriend(Long targetUserId, Long friendId) {

		}

		@Override
		public void removeFriend(Long targetUserId, Long friendId) {

		}

		@Override
		public List<Long> getAllFriends(User user) {
			return null;
		}

		@Override
		public List<User> getListOfFriends(Long id) {
			return null;
		}

		@Override
		public List<User> getCommonFriends(Long id, Long otherId) {
			return null;
		}
	}));



	@Test
	void shouldSetNameAsLoginWhenEmptyName() throws MyValidateExeption {
		User user = new User();
		user.setEmail("ya@ya.ru");
		user.setLogin("Ya");
		user.setBirthday(LocalDate.of(2014, 11, 11));
		userController.postUser(user);
		assertEquals("Ya", user.getName());
	}

	@Test
	void addUserShouldValidateUserLoginWithSpaces() {
		User user = new User();
		user.setEmail("ya@ya.ru");
		user.setBirthday(LocalDate.of(2000,12,12));
		user.setLogin("Ya molodec");

		Exception exception = assertThrows(MyValidateExeption.class, () -> userController.postUser(user));
		assertEquals("Login can't be empty", exception.getMessage());
	}


	@Test
	void filmWithOldReleaseDateTest() {
		Film film = new Film();
		film.setName("Test");
		film.setDescription("TestTest");
		film.setDuration(22);
		film.setReleaseDate(LocalDate.of(1, 1, 1));
		Exception exception = assertThrows(MyValidateExeption.class, () -> filmController.postFilm(film));
		assertEquals("Film release date must be older than 28.12.1895", exception.getMessage());
	}

}
