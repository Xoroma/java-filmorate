package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository("GenreDAO")
@Primary
@AllArgsConstructor
public class GenreDao {
    private final JdbcTemplate jdbcTemplate;
    public Genre getGenre(int id) {
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject("SELECT * FROM genres WHERE genre_id = " + id,
                    new GenreMapper());
        } catch (DataAccessException e) {
            throw new GenreNotFoundException("Ошибка получения жанра: не найден жанр с id=" + id);
        }

        return genre;
    }

    public List<Genre> getGenres() {
        return jdbcTemplate.query("SELECT * FROM genres", new GenreMapper());
    }

    public List<Genre> getFilmGenres(int id) {
        String sql = "SELECT * FROM film_genres " +
                "JOIN genres on genres.genre_id = film_genres.genre_id WHERE film_id = " + id;
        return jdbcTemplate.query(sql , new GenreMapper());
    }

    public void insertGenres(Film film, int id) {
        try {
            List<Object[]> list = new ArrayList<>();

            for (Genre genre : film.getGenres()) {
                Object[] arr = {id, genre.getId()};
                list.add(arr);
            }
            jdbcTemplate.batchUpdate("MERGE INTO film_genres (film_id, genre_id) VALUES (?, ?)", list);
        } catch (DataIntegrityViolationException e) {
            throw new GenreNotFoundException("Ошибка при добавлении фильма в БД: один или несколько идентификаторов жанров не найдены.");
        }
    }

    static class GenreMapper implements RowMapper<Genre> {
        @Override
        public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Genre(rs.getInt("genre_id"), rs.getString("genre_name"));
        }
    }
}
