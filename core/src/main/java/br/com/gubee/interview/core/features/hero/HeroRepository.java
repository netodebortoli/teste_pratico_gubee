package br.com.gubee.interview.core.features.hero;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.model.HeroDTO;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class HeroRepository {

    private static final String BLANK_SPACE = " ";

    private static final String FIND_ALL_QUERY = "SELECT h.name, h.race, ps.strength," +
            " ps.agility, ps.dexterity, ps.intelligence" +
            " FROM hero h INNER JOIN power_stats ps ON ps.id = h.power_stats_id";

    private static final String CREATE_QUERY = "INSERT INTO hero" +
            " (name, race, power_stats_id) VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String UPDATE_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, updated_at = :updatedAt" +
            " WHERE id = :id";

    private static final String FIND_BY_ID_QUERY = "SELECT * FROM hero WHERE id = :id";

    private static final String FIND_BY_NAME_QUERY = "SELECT * FROM hero WHERE lower(name) LIKE lower(:name) LIMIT 1";

    private static final String ROW_COUNT_QUERY = "SELECT count(id) FROM hero h";

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM hero WHERE id = :id";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private Integer getTotalRows(String sql, Map<String, Object> params) {
        return namedParameterJdbcTemplate.query(
                sql,
                params,
                (rs) -> {
                    if (rs.next())
                        return rs.getInt("count");
                    else
                        return null;
                });
    }

    private Integer getTotalRows() {
        return namedParameterJdbcTemplate.query(
                ROW_COUNT_QUERY,
                (rs) -> {
                    if (rs.next())
                        return rs.getInt("count");
                    else
                        return null;
                });
    }

    private String buildSqlPagination(Pageable page) {
        StringBuilder sqlPage = new StringBuilder();
        sqlPage.append("LIMIT").append(BLANK_SPACE);
        sqlPage.append(page.getPageSize()).append(BLANK_SPACE);
        sqlPage.append("OFFSET").append(BLANK_SPACE);
        sqlPage.append(page.getOffset());
        return sqlPage.toString();
    }

    public Page<HeroDTO> findAll(Pageable page) {
        Integer totalElements = getTotalRows();

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(FIND_ALL_QUERY).append(BLANK_SPACE);
        sqlBuilder.append(buildSqlPagination(page));

        List<HeroDTO> heroes = namedParameterJdbcTemplate.query(
                sqlBuilder.toString(), new MapperHero.MapperToDTO());
        return new PageImpl<HeroDTO>(heroes, page, totalElements);
    }

    public Page<HeroDTO> findAll(String filteredName, Pageable page) {
        final Map<String, Object> params = Map.of("name", filteredName);
        final String sqlFilter = "WHERE lower(h.name) LIKE lower(concat('%',:name,'%'))";

        StringBuilder sqlCountRow = new StringBuilder();
        sqlCountRow.append(ROW_COUNT_QUERY).append(BLANK_SPACE);
        sqlCountRow.append(sqlFilter);
        Integer totalElements = getTotalRows(sqlCountRow.toString(), params);

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append(FIND_ALL_QUERY).append(BLANK_SPACE);
        sqlBuilder.append(sqlFilter).append(BLANK_SPACE);
        sqlBuilder.append(buildSqlPagination(page));
        List<HeroDTO> heroes = namedParameterJdbcTemplate.query(sqlBuilder.toString(), params,
                new MapperHero.MapperToDTO());
        return new PageImpl<HeroDTO>(heroes, page, totalElements);
    }

    public UUID create(Hero hero) {
        final Map<String, Object> params = Map.of(
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "powerStatsId", hero.getPowerStatsId());
        return namedParameterJdbcTemplate.queryForObject(CREATE_QUERY, params, UUID.class);
    }

    public boolean update(Hero hero) {
        final Map<String, Object> params = Map.of(
                "name", hero.getName(),
                "race", hero.getRace().name(),
                "updatedAt", OffsetDateTime.now(ZoneOffset.UTC),
                "id", hero.getId());
        return namedParameterJdbcTemplate.update(UPDATE_QUERY, params) != 0;
    }

    public Hero findById(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.query(
                FIND_BY_ID_QUERY,
                params,
                (rs) -> {
                    if (rs.next())
                        return new MapperHero.MapperToEntity().mapRow(rs, 1);
                    else
                        return null;
                });
    }

    public Hero findByName(String name) {
        final Map<String, Object> params = Map.of("name", name);
        return namedParameterJdbcTemplate.query(
                FIND_BY_NAME_QUERY,
                params,
                (rs) -> {
                    if (rs.next())
                        return new MapperHero.MapperToEntity().mapRow(rs, 1);
                    else
                        return null;
                });
    }

    public boolean delete(UUID id) {
        final Map<String, Object> params = Map.of("id", id);
        return namedParameterJdbcTemplate.update(DELETE_BY_ID_QUERY, params) != 0;
    }

}
