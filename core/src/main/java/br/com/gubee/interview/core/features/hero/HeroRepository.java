package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.entity.HeroEntity;
import br.com.gubee.interview.entity.enums.Race;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String FIND_HERO_QUERY_BD_ID = "SELECT * FROM hero WHERE id = :id";

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id)" +
            " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID create(HeroEntity hero) {
        final Map<String, Object> params = Map.of(
                "name", hero.getName(),
                "race", hero.getRace(),
                "powerStatsId", hero.getPowerStatsId());

        return namedParameterJdbcTemplate.queryForObject(
                CREATE_HERO_QUERY,
                params,
                UUID.class);
    }

    HeroEntity findById(UUID id) {
        final Map<String, Object> param = Map.of("id", id);
        return namedParameterJdbcTemplate.queryForObject(
                FIND_HERO_QUERY_BD_ID,
                param,
                new HeroMapper()
        );
    }

    private static class HeroMapper implements RowMapper<HeroEntity> {
        @Override
        public HeroEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
            return HeroEntity.builder()
                    .id(UUID.fromString(rs.getString("powerStatsId")))
                    .name(rs.getString("name"))
                    .race(Race.valueOf(rs.getString("race")))
                    .powerStatsId(UUID.fromString(rs.getString("powerStatsId")))
                    .build();
        }
    }

}

