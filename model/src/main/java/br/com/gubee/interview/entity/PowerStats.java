package br.com.gubee.interview.entity;

import static lombok.AccessLevel.PRIVATE;

import java.time.Instant;
import java.util.UUID;

import br.com.gubee.interview.entity.model.PowerStatsDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = PRIVATE)
@AllArgsConstructor
@Builder
public class PowerStats {

    private UUID id;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;

    public PowerStats(PowerStatsDTO powerStats) {
        this.strength = powerStats.getStrength();
        this.agility = powerStats.getAgility();
        this.dexterity = powerStats.getDexterity();
        this.intelligence = powerStats.getIntelligence();
    }
}
