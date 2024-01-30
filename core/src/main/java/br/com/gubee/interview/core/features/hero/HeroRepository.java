package br.com.gubee.interview.core.features.hero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.HeroDTO;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String FIND_ALL_QUERY = "SELECT h.name, h.race, ps.strength," +
            " ps.agility, ps.dexterity, ps.intelligence" +
            " FROM hero h INNER JOIN power_stats ps ON ps.id = h.power_stats_id";

    private static final String CREATE_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id) VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String UPDATE_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, power_stats_id = :powerStatsId, updated_at = :updatedAt" +
            " WHERE id = :id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM hero WHERE id = :id";

    private static final String FIND_BY_NAME_IF_EXISTS_QUERY = "SELECT * FROM hero WHERE lower(name) LIKE lower(:name) LIMIT 1";

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM hero WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    List<HeroDTO> findAll(String filter) {
        if (filter != null && StringUtils.hasText(filter)) {
            return findAllWithFilterName(filter);
        }
        return namedParameterJdbcTemplate.query(
                FIND_ALL_QUERY,
                new HeroDTOMapper());
    }

    private List<HeroDTO> findAllWithFilterName(String filteredName) {
        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(FIND_ALL_QUERY);
        sqlBuilder.append(" WHERE lower(h.name) LIKE lower(concat('%',:name,'%'))");
        final Map<String, Object> params = Map.of("name", filteredName);
        return namedParameterJdbcTemplate.query(
                sqlBuilder.toString(),
                params,
                new HeroDTOMapper());
    }

    UUID create(Hero hero) {
        final Map<String, Object> params = Map.of(
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());
        return namedParameterJdbcTemplate.queryForObject(
                CREATE_QUERY,
                params,
                UUID.class);
    }

    Hero update(Hero hero) {
        final Map<String, Object> params = Map.of(
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId(),
                "updatedAt", hero.getUpdatedAt(),
                "id", hero.getId());
        return namedParameterJdbcTemplate.queryForObject(
                UPDATE_QUERY,
                params,
                new HeroMapper());
    }

    Hero findById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.query(
                FIND_BY_ID_QUERY,
                params,
                new ResultSetExtractor<Hero>() {
                    @Override
                    public Hero extractData(ResultSet rs) throws SQLException, DataAccessException {
                        if (rs.next()) {
                            return new HeroMapper().mapRow(rs, 1);
                        } else {
                            return null;
                        }
                    }
                });
    }

    boolean findByNameIfExists(String name) {
        final Map<String, Object> params = Map.of("name", name);
        return namedParameterJdbcTemplate.query(
                FIND_BY_NAME_IF_EXISTS_QUERY,
                params,
                (rs) -> {
                    if (rs.next())
                        return true;
                    else
                        return false;
                });
    }

    void delete(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        namedParameterJdbcTemplate.update(
                DELETE_BY_ID_QUERY,
                params);
    }

    private static class HeroMapper implements RowMapper<Hero> {
        @Override
        public Hero mapRow(ResultSet rs, int rowNum) throws SQLException {
            return Hero.builder()
                    .id(UUID.fromString(rs.getString("id")))
                    .name(rs.getString("name"))
                    .race(Race.valueOf(rs.getString("race")))
                    .powerStatsId(UUID.fromString(rs.getString("power_stats_id")))
                    .build();
        }
    }

    private static class HeroDTOMapper implements RowMapper<HeroDTO> {
        @Override
        public HeroDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
            return HeroDTO.builder()
                    .name(rs.getString("name"))
                    .race(Race.valueOf(rs.getString("race")))
                    .strength(rs.getInt("strength"))
                    .agility(rs.getInt("agility"))
                    .dexterity(rs.getInt("dexterity"))
                    .intelligence(rs.getInt("intelligence"))
                    .build();
        }
    }

}
