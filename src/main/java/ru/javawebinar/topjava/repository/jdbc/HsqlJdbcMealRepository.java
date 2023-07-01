package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.Profiles;

import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * @author Andrei Durkin <a.durkin@goodt.me> at 01.07.2023
 */

@Repository
@Profile(Profiles.HSQL_DB)
public class HsqlJdbcMealRepository extends AbstractJdbcMealRepository<Timestamp> {
    public HsqlJdbcMealRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        super(jdbcTemplate, namedParameterJdbcTemplate);
    }

    @Override
    protected Timestamp convertToDbTime(LocalDateTime localDateTime) {
        return Timestamp.valueOf(localDateTime);
    }
}
