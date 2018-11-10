package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import static ru.javawebinar.topjava.Profiles.HSQL_DB;

@Repository
@Profile(HSQL_DB)
public class HsqlJdbcMealRepositoryImpl extends JdbcMealRepository {
    @Autowired
    public HsqlJdbcMealRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected MapSqlParameterSource getMapSqlParameterSource(Meal meal, int userId) {
        return super.getMapSqlParameterSource(meal, userId)
                .addValue("date_time", Timestamp.valueOf(meal.getDateTime()));
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return jdbcTemplate.query(
                "SELECT * FROM meals WHERE user_id=?  AND date_time BETWEEN ? AND ? ORDER BY date_time DESC",
                ROW_MAPPER, userId, Timestamp.valueOf(startDate), Timestamp.valueOf(endDate));
    }
}