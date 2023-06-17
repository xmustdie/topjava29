package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class JdbcMealRepository implements MealRepository {
    private static final BeanPropertyRowMapper<Meal> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Meal.class);

    private static final String UPDATE_QUERY = "update meals " +
            "set datetime=:datetime, description=:description, calories=:calories " +
            "where id=:id and user_id=:userId";

    private static final String DELETE_QUERY = "delete from meals where id=? and user_id=?";

    private static final String GET_QUERY = "SELECT * FROM meals WHERE id=? AND user_id=?";

    private static final String GET_ALL_DESC_QUERY = "SELECT * FROM meals WHERE user_id=? ORDER BY datetime DESC";

    private static final String GET_ALL_FILTERED_DESC_QUERY = "SELECT * FROM meals " +
            "WHERE user_id=? AND datetime>=? AND datetime<? ORDER BY datetime DESC";

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public JdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("meals")
                .usingGeneratedKeyColumns("id");
        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        final MapSqlParameterSource parameterSource = new MapSqlParameterSource()
                .addValue("id", meal.getId())
                .addValue("datetime", meal.getDateTime())
                .addValue("description", meal.getDescription())
                .addValue("calories", meal.getCalories())
                .addValue("userId", userId);
        if (meal.isNew()) {
            final Number key = jdbcInsert.executeAndReturnKey(parameterSource);
            meal.setId(key.intValue());
            return meal;
        }
        return namedParameterJdbcTemplate.update(UPDATE_QUERY, parameterSource) == 0 ? null : meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        return jdbcTemplate.update(DELETE_QUERY, id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = jdbcTemplate.query(GET_QUERY, ROW_MAPPER, id, userId);
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return jdbcTemplate.query(GET_ALL_DESC_QUERY, ROW_MAPPER, userId);
    }

    @Override
    public List<Meal> getBetweenHalfOpen(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return jdbcTemplate.query(GET_ALL_FILTERED_DESC_QUERY, ROW_MAPPER, userId, startDateTime, endDateTime);
    }
}
