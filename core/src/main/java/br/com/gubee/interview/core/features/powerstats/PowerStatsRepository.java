package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.entity.PowerStatsEntity;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String FIND_POWER_BY_ID = "SELECT * FROM power_stats WHERE id = :id";

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStatsEntity powerStats) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(powerStats),
            UUID.class);
    }

    PowerStats findById(UUID id) {
        Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                FIND_POWER_BY_ID,
                params,
                new PowerStatsMapper()
        );
    }

    private static class PowerStatsMapper implements RowMapper<PowerStats> {
        @Override
        public PowerStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PowerStats.builder()
                    .strength(rs.getInt("strength"))
                    .intelligence(rs.getInt("intelligence"))
                    .dexterity(rs.getInt("dexterity"))
                    .agility(rs.getInt("agility"))
                    .build();
        }
    }
}
