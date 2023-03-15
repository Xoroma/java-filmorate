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
import ru.yandex.practicum.filmorate.exeptions.FilmNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.LikeNotFoundExeption;
import ru.yandex.practicum.filmorate.exeptions.MyValidateExeption;
import ru.yandex.practicum.filmorate.exeptions.UserNotFoundExeption;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;
import java.util.stream.Collectors;


@Primary
@Repository("FilmDAO")
@AllArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreDao genreDao;

    private final MpaDAO mpaDAO;


    @Override
    public Film getFilm(int filmId) {
        Film film;
        String sql ="SELECT * FROM films JOIN mpaRating ON films.mpaRating_id = mpaRating.mpa_id WHERE film_id = ";
        try{
            film = jdbcTemplate.queryForObject(sql + filmId, new FilmMapper());
        }catch(DataAccessException e){
            throw new FilmNotFoundExeption(String.format("Ошибка получения фильма из БД с id = %d", filmId));
        }
        return film;
    }

    @Override
    public List<Film> getFilms() {
        String sql = "SELECT * FROM films JOIN mpaRating ON films.mpaRating_id = mpaRating.mpa_id";
        return jdbcTemplate.query(sql, new FilmMapper());
    }

    @Override
    public Film postFilm(Film film) throws MyValidateExeption {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        Number id;

        jdbcTemplate.update(connnection -> {
            PreparedStatement ps = connnection.prepareStatement(
                    "INSERT INTO films (" +
                            "film_name, " +
                            "description, " +
                            "release_date, " +
                            "duration, " +
                            "mpaRating_id, "+
                            "rate)"+
                            "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setInt(4, film.getDuration());
            ps.setInt(5, film.getMpa().getId());
            ps.setInt(6, film.getRate());
            return ps;
        }, keyHolder);

        id = keyHolder.getKey();
        if (id == null) {
            throw new RuntimeException("Ошибка: фильм не был добавлен.");
        }


        genreDao.insertGenres(film, id.intValue());


        return getFilm(id.intValue());

    }

    @Override
    public Film updateFilm(Film film) throws MyValidateExeption {
        Integer responseFilmId = getFilmIDFromDB(film.getId());
        String sql =  "UPDATE films SET film_name = ?, description = ?, release_date =?, duration =?, mpaRating_id=?, rate = ?"+
                "WHERE film_id = ?";
        if(responseFilmId == null){
            throw  new FilmNotFoundExeption("Фильм с таким id не найден в БД");
        }
        jdbcTemplate.update(sql,film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());

        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", film.getId());


        genreDao.insertGenres(film, film.getId());


        return getFilm(film.getId());
    }

    private Integer getFilmIDFromDB(int id){
        Integer resultfilmId;
        String sql = "SELECT film_id FROM films WHERE film_id = ?";
        try{
            resultfilmId = jdbcTemplate.queryForObject(sql, Integer.class, id);
        }catch(EmptyResultDataAccessException e){
            resultfilmId = null;
        }
        return  resultfilmId;
    }

    @Override
    public void addLike(Integer targetFilmId, Integer userId) {
        Integer responseId = getFilmIDFromDB(targetFilmId);
        String sql = "INSERT INTO likes (film_id, from_user_id)  VALUES (?, ?)";
        if (responseId == null) {
            throw new FilmNotFoundExeption(
                    String.format("Ошибка при добавлении лайка: фильм с id=%d не найден.", targetFilmId));
        }

        try {
            jdbcTemplate.update(sql, targetFilmId, userId);
        } catch (DataIntegrityViolationException e) {
            throw new UserNotFoundExeption(
                    String.format("Ошибка при добавлении лайка для фильма с id=%d от пользователя с id=%d: пользователь не найден.",
                            targetFilmId, userId));
        }
    }


    @Override
    public void removeLike(Integer targetFilmId, Integer userId) {
        Integer responseId = getFilmIDFromDB(targetFilmId);
        if (responseId == null) {
            throw new FilmNotFoundExeption(
                    String.format("Ошибка при удалении лайка: фильм с id=%d не найден.", targetFilmId));
        }

        try {
            String sql = "SELECT film_id FROM likes WHERE film_id = ? AND from_user_id = ?";
            jdbcTemplate.queryForObject(sql, Integer.class, targetFilmId, userId);

        } catch (EmptyResultDataAccessException e) {
            throw new LikeNotFoundExeption(
                    String.format("Ошибка при удалении лайка: " +
                            "лайк к фильму с id=%d от пользователя с id=%d не найден.", targetFilmId, userId));
        }

        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ? AND from_user_id = ?", targetFilmId, userId);
    }

    @Override
    public List<Film> getMostPopularFilms(Integer count) {
        List<Film> popularFilmsList = getFilms();

        popularFilmsList = popularFilmsList.stream()
                .sorted(Comparator.comparing(Film::getRate))
                .limit(count)
                .collect(Collectors.toList());

        return popularFilmsList;
    }

    @Override
    public Genre getGenre(int id) {
        return  genreDao.getGenre(id);
    }

    @Override
    public List<Genre> getListOfGenres() {
        return genreDao.getGenres();
    }

    @Override
    public MPA getMPA(int id) {
        return mpaDAO.getMPA(id);
    }

    @Override
    public List<MPA> getListOfMPAs() {
        return mpaDAO.getMPAs();
    }

    @Override
    public void removeFilm(int id) {
        Integer responseId = getFilmIDFromDB(id);

        if (responseId == null) {
            throw new FilmNotFoundExeption(String.format("Ошибка удаления: фильм с id=%d не найден.", id));
        }

        jdbcTemplate.update("DELETE FROM films WHERE film_id = ? ", id);
    }


    class FilmMapper implements RowMapper<Film>{

        @Override
        public Film mapRow(ResultSet rs, int rowNum) throws SQLException {
            int filmId = rs.getInt("film_id");
            Film film = new Film(filmId,
                    rs.getString("film_name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name")),
                    rs.getInt("rate"));

            List<Genre> genres = genreDao.getFilmGenres(filmId);
            for(Genre genre: genres){
                film.addGenre(genre);
            }

            addLikeToFilmFromDB(film, filmId);

            return film;
        }
    }

    private void addLikeToFilmFromDB(Film film, int id){
        String sql = "SELECT from_user_id FROM likes WHERE film_id = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, id);
        while(sqlRowSet.next()){
            film.addLike(sqlRowSet.getInt("from_user_id"));
        }
    }
}
