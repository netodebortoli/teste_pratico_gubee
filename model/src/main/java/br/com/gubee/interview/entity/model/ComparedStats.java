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

    public static final int STRENGHT = 1;
    public static final int AGILITY = 2;
    public static final int DEXTERITY = 3;
    public static final int INTELLIGENCE = 4;

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

    public String getStatsValue(int statsName) {
        switch (statsName) {
            case ComparedStats.STRENGHT:
                return getStrength();
            case ComparedStats.AGILITY:
                return getAgility();
            case ComparedStats.DEXTERITY:
                return getDexterity();
            case ComparedStats.INTELLIGENCE:
                return getIntelligence();
            default:
                throw new IllegalArgumentException("Status inválido: " + statsName);
        }
    }

    public void setStatsValue(int statsName, String statsValue) {
        switch (statsName) {
            case ComparedStats.STRENGHT:
                setStrength(statsValue);
                break;
            case ComparedStats.AGILITY:
                setAgility(statsValue);
                break;
            case ComparedStats.DEXTERITY:
                setDexterity(statsValue);
                break;
            case ComparedStats.INTELLIGENCE:
                setIntelligence(statsValue);
                break;
            default:
                throw new IllegalArgumentException("Status inválido: " + statsName);
        }
    }

}
