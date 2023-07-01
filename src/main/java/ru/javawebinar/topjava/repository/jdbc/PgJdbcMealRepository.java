package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.time.LocalDateTime;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 01.07.2023
 */

@Repository
@Profile(Profiles.POSTGRES_DB)
public class PgJdbcMealRepository extends AbstractJdbcMealRepository<LocalDateTime> {
    public PgJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected LocalDateTime convertToDbTime(LocalDateTime localDateTime) {
        return localDateTime;
    }
}
