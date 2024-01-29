package br.com.gubee.interview.core.features.powerstats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.entity.PowerStats;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepository {

    private static final String CREATE_QUERY = "INSERT INTO power_stats" +
            " (strength, agility, dexterity, intelligence)" +
            " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String UPDATE_QUERY = "UPDATE power_stats " +
            " SET strength = :strength, agility = :agility, dexterity = :dexterity, intelligence = :intelligence, updated_at = :updatedAt"
            +
            " WHERE id = :id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM power_stats WHERE id = :id";

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM power_stats WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(PowerStats powerStats) {
        Map<String, Object> params = Map.of(
                "strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity", powerStats.getDexterity(),
                "intelligence", powerStats.getIntelligence());
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_QUERY,
                params,
                UUID.class);
    }

    PowerStats update(PowerStats powerStats) {
        Map<String, Object> params = Map.of(
                "strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity", powerStats.getDexterity(),
                "intelligence", powerStats.getIntelligence(),
                "updatedAt", powerStats.getUpdatedAt(),
                "id", powerStats.getId());
        return namedParameterJdbcTemplate.queryForObject(
                UPDATE_QUERY,
                params,
                new PowerStatsMapper());
    }

    PowerStats findById(UUID id) {
        Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.query(
                FIND_BY_ID_QUERY,
                params,
                new ResultSetExtractor<PowerStats>() {
                    @Override
                    public PowerStats extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return new PowerStatsMapper().mapRow(rs, 1);
                        } else {
                            return null;
                        }
                    }
                }
        );
    }

    void delete(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_BY_ID_QUERY,
                params);
    }

    private static class PowerStatsMapper implements RowMapper<PowerStats> {
        @Override
        public PowerStats mapRow(ResultSet rs, int rowNum) throws SQLException {
            return PowerStats.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .strength(rs.getInt("strength"))
                    .agility(rs.getInt("agility"))
                    .dexterity(rs.getInt("dexterity"))
                    .intelligence(rs.getInt("intelligence"))
                    .build();
        }
    }
}
