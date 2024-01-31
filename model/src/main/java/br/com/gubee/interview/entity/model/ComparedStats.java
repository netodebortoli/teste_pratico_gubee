package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class ComparedStats {

    private String strength;

    private String agility;

    private String dexterity;

    private String intelligence;

    public ComparedStats(PowerStatsDTO powerStats) {
        this.strength = String.valueOf(powerStats.getStrength());
        this.agility = String.valueOf(powerStats.getAgility());
        this.dexterity = String.valueOf(powerStats.getDexterity());
        this.intelligence = String.valueOf(powerStats.getIntelligence());
    }

}
