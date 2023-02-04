package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmorateApplicationTests {

	@Test
	void contextLoads() {
	}

	FilmController filmController = new FilmController();
	UserController userController = new UserController();



	@Test
	void shouldSetNameAsLoginWhenEmptyName() throws MyValidateExeption {
		User user = new User();
		user.setEmail("ya@ya.ru");
		user.setLogin("Ya");
		user.setBirthday(LocalDate.of(2014, 11, 11));
		userController.postItem(user);
		assertEquals("Ya", user.getName());
	}

	@Test
	void addUserShouldValidateUserLoginWithSpaces() {
		User user = new User();
		user.setEmail("ya@ya.ru");
		user.setBirthday(LocalDate.of(2000,12,12));
		user.setLogin("Ya molodec");

		Exception exception = assertThrows(MyValidateExeption.class, () -> userController.postItem(user));
		assertEquals("Login can't be empty", exception.getMessage());
	}


	@Test
	void filmWithOldReleaseDateTest() {
		Film film = new Film();
		film.setName("Test");
		film.setDescription("TestTest");
		film.setDuration(22);
		film.setReleaseDate(LocalDate.of(1, 1, 1));
		Exception exception = assertThrows(MyValidateExeption.class, () -> filmController.postItem(film));
		assertEquals("Film release date must be older than 28.12.1895", exception.getMessage());
	}

}
