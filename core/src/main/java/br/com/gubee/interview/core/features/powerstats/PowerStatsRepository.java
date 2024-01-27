package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.entity.PowerStatsEntity;
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

    private static final String CREATE_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM power_stats WHERE id = :id";

    private static final String DELETE_BY_ID_QUERY = "DELETE * power_stats WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStatsEntity powerStats) {
        Map<String, Object> params = Map.of(
                "strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity", powerStats.getDexterity(),
                "intelligence", powerStats.getIntelligence()
        );
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_QUERY,
                params,
                UUID.class);
    }

    PowerStatsEntity findById(UUID id) {
        Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                FIND_BY_ID_QUERY,
                params,
                new PowerStatsMapper()
        );
    }

    void delete(UUID id) {
        final Map<String, Object> param = Map.of("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_BY_ID_QUERY,
                param
        );
    }

    private static class PowerStatsMapper implements RowMapper<PowerStatsEntity> {
        @Override
        public PowerStatsEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PowerStatsEntity.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .strength(rs.getInt("strength"))
                    .intelligence(rs.getInt("intelligence"))
                    .dexterity(rs.getInt("dexterity"))
                    .agility(rs.getInt("agility"))
                    .build();
        }
    }
}
