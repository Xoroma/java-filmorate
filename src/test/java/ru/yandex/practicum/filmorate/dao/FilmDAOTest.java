package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@AllArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Sql(scripts = "classpath:testSchema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:testData.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmDAOTest {

    FilmDbStorage filmStorage;
    UserDbStorage userStorage;


    @Test
    public void postFilmTest() throws MyValidateExeption {
        Film film = makeDefaultFilm();
        film.setId(1);

        assertEquals(film, filmStorage.postFilm(film));
    }

    @Test
    public void shouldThrowExceptionForGetNonExistentFilm() {
        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.getFilm(1));
    }

    @Test
    public void getFilmsTest() throws MyValidateExeption {
        Film film1 = makeDefaultFilm();
        film1.setId(1);
        Film film2 = makeDefaultFilm();
        film2.setId(2);
        filmStorage.postFilm(film1);
        filmStorage.postFilm(film2);
        assertEquals(List.of(film1, film2), filmStorage.getFilms());
    }


    @Test
    public void shouldThrowExceptionForAddLikeForNonExistentFilm() throws MyValidateExeption {
        User user = makeDefaultUser();
        userStorage.postUser(user);

        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.addLike(1, 1));
    }

    @Test
    public void shouldThrowExceptionForAddingLikeFromNonExistentUser() throws MyValidateExeption {
        Film film = makeDefaultFilm();
        filmStorage.postFilm(film);

        assertThrows(UserNotFoundExeption.class, () -> filmStorage.addLike(1, 2));
    }

    @Test
    public void shouldNotAddFilmWithIncorrectData() throws MyValidateExeption {
        Film film1 = new Film(
                0,
                "TESt Name",
                "test descr",
                LocalDate.parse("1978-02-15"),
                120,
                new MPA(filmStorage.getListOfMPAs().size() + 1, "SOMEText"),
                0
                );
        assertThrows(DataIntegrityViolationException.class, () -> filmStorage.postFilm(film1));
        Film film2 = makeDefaultFilm();
        film2.setRate(-4);
        filmStorage.postFilm(film2);
        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.getFilm(1));
    }

    @Test
    public void updateFilmTest() throws MyValidateExeption {
        Film film = makeDefaultFilm();
        filmStorage.postFilm(film);
        film.setId(1);
        film.setRate(2);
        filmStorage.updateFilm(film);

        assertEquals(film, filmStorage.getFilm(1));
    }



    @Test
    public void removeFilmTest() throws MyValidateExeption {
        Film film = makeDefaultFilm();
        User user = makeDefaultUser();
        filmStorage.postFilm(film);
        film.setId(1);
        assertEquals(film, filmStorage.getFilm(1));
        userStorage.postUser(user);
        user.setId(1);
        filmStorage.addLike(1, 1);

        filmStorage.removeFilm(1);
        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.getFilm(1));
        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.removeLike(1, 1));
        assertEquals(user, userStorage.getUser(1));
    }

    @Test
    public void shouldThrowExceptionForRemovingNonExistentLike() {
        assertThrows(FilmNotFoundExeption.class, () -> filmStorage.removeLike(1, 1));
    }


    private Film makeDefaultFilm() {
        return new Film(
                0,
                "FilmTestName",
                "FilmTestDescription",
                LocalDate.parse("2021-04-11"),
                120,
                new MPA(1, "G"),
                0
                );
    }

    private User makeDefaultUser() {
        return new User(
                0,
                "TestName",
                "test@test.ru",
                "",
                LocalDate.parse("1988-04-12"));
    }
}
