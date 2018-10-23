package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepositoryImpl implements MealRepository {

    private static final RowMapper<Meal> ROW_MAPPER = (rs, rowNum) -> {
        Meal meal = new Meal();
        meal.setId(rs.getInt("id"));
        meal.setDateTime(rs.getTimestamp("datetime").toLocalDateTime());
        meal.setDescription(rs.getString("description"));
        meal.setCalories(rs.getInt("calories"));
        return meal;
    };

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Meal save(Meal meal, int userId) {
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update("DELETE FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query("SELECT * FROM meals WHERE id=? AND user_id=?", ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return null;
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return null;
    }
}
