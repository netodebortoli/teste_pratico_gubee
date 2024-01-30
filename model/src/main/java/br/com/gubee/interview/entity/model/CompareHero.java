package br.com.gubee.interview.entity.model;

import static lombok.AccessLevel.PRIVATE;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = PRIVATE)
public class CompareHero {

    private UUID idHeroOne;

    private PowerStatsDTO powerStatsHeroOne;

    private UUID idHeroTwo;

    private PowerStatsDTO powerStatsHeroTwo;

    public CompareHero(UUID idHeroOne, UUID idHeroTwo) {
        this.idHeroOne = idHeroOne;
        this.idHeroTwo = idHeroTwo;
    }

}
