package ru.yandex.practicum.filmorate.dao;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.MPA;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository("MpaDAO")
@Primary
@AllArgsConstructor
public class MpaDAO {

    private final JdbcTemplate jdbcTemplate;

    public MPA getMPA(int id) {
        MPA mpa;
        try {
            mpa = jdbcTemplate.queryForObject("SELECT * FROM mpaRating WHERE mpa_id = " + id, new MPAMapper());
        } catch (DataAccessException e) {
            throw new MpaNotFoundException("Ошибка получения рейтинга: не найден возрастной рейтинг с id= " + id);
        }
        return mpa;
    }

    public List<MPA> getMPAs() {
        return jdbcTemplate.query("SELECT * FROM mpaRating", new MPAMapper());
    }



    static class MPAMapper implements RowMapper<MPA> {
        @Override
        public MPA mapRow(ResultSet rs, int rowNum) throws SQLException, SQLException {
            return new MPA(rs.getInt("mpa_id"), rs.getString("mpa_name"));
        }
    }
}