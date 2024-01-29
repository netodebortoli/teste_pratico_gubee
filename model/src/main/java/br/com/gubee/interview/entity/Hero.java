package br.com.gubee.interview.entity;

import static lombok.AccessLevel.PRIVATE;

import java.time.Instant;
import java.util.UUID;

import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.HeroDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class Hero {

    private UUID id;
    private String name;
    private Race race;
    private UUID powerStatsId;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;

    public Hero(HeroDTO hero, UUID powerStatsId) {
        this.name = hero.getName();
        this.race = hero.getRace();
        this.powerStatsId = powerStatsId;
    }
}
