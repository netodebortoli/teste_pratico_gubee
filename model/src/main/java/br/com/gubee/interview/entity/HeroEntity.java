package br.com.gubee.interview.entity;

import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.Hero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

import static lombok.AccessLevel.PRIVATE;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class HeroEntity {

    private UUID id;
    private String name;
    private Race race;
    private UUID powerStatsId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;

    public HeroEntity(Hero hero, UUID powerStatsId) {
        this.name = hero.getName();
        this.race = hero.getRace();
        this.powerStatsId = powerStatsId;
    }
}
