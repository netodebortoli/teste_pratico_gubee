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

    private UUID heroOneId;

    private ComparedStats powerStatsHeroOne;

    private UUID heroTwoId;

    private ComparedStats powerStatsHeroTwo;

    public CompareHero(UUID heroOneId, UUID heroTwoId) {
        this.heroOneId = heroOneId;
        this.heroTwoId = heroTwoId;
    }

}
