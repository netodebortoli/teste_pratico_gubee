package br.com.gubee.interview.entity;

import br.com.gubee.interview.entity.model.Hero;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Builder
public class PowerStatsEntity {

    private UUID id;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;

    public PowerStatsEntity(PowerStats powerStats) {
        this.strength = powerStats.getStrength();
        this.agility = powerStats.getAgility();
        this.dexterity = powerStats.getDexterity();
        this.intelligence = powerStats.getIntelligence();
    }
}
