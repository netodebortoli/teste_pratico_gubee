package br.com.gubee.interview.core.features.powerstats;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import org.springframework.jdbc.core.RowMapper;

import br.com.gubee.interview.entity.PowerStats;

public final class MapperPowerStats {

    public static class MapperToEntity implements RowMapper<PowerStats> {
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
