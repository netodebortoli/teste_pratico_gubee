package br.com.gubee.interview.core.features.powerstats;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.UUID;

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
            " SET strength = :strength, agility = :agility, dexterity = :dexterity," +
            " intelligence = :intelligence, updated_at = :updatedAt" +
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
        return namedParameterJdbcTemplate.queryForObject(CREATE_QUERY, params, UUID.class);
    }

    boolean update(PowerStats powerStats) {
        Map<String, Object> params = Map.of(
                "strength", powerStats.getStrength(),
                "agility", powerStats.getAgility(),
                "dexterity", powerStats.getDexterity(),
                "intelligence", powerStats.getIntelligence(),
                "updatedAt", OffsetDateTime.now(ZoneOffset.UTC),
                "id", powerStats.getId());
        return namedParameterJdbcTemplate.update(UPDATE_QUERY, params) != 0;
    }

    PowerStats findById(UUID id) {
        Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.query(
                FIND_BY_ID_QUERY,
                params,
                (rs) -> {
                    if (rs.next())
                        return new MapperPowerStats.MapperToEntity().mapRow(rs, 1);
                    else
                        return null;
                });
    }

    boolean delete(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.update(DELETE_BY_ID_QUERY, params) != 0;
    }

}
