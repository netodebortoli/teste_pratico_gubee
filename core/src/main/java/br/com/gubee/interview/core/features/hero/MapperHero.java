package br.com.gubee.interview.core.features.hero;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.HeroDTO;

public class MapperHero {

    public static class MapperToDTO implements RowMapper<HeroDTO> {
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

    public static class MapperToEntity implements RowMapper<Hero> {
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

}
