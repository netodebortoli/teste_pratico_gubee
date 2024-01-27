package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.entity.HeroEntity;
import br.com.gubee.interview.entity.PowerStatsEntity;
import br.com.gubee.interview.entity.model.Hero;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final PowerStatsService powerStatsService;

    @Transactional(rollbackFor = {Exception.class})
    public UUID create(@Valid @NotNull Hero hero) {
        PowerStatsEntity powerStatsEntity = new PowerStatsEntity(hero);
        UUID powerStatsId = powerStatsService.create(powerStatsEntity);
        return heroRepository.create(new HeroEntity(hero, powerStatsId));
    }

    public Hero findById(@NotNull @Positive UUID id) {
        HeroEntity heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        PowerStats powerStats = powerStatsService.findById(heroEntity.getPowerStatsId());
        return Hero.builder()
                .name(heroEntity.getName())
                .race(heroEntity.getRace())
                .strength(powerStats.getStrength())
                .intelligence(powerStats.getIntelligence())
                .dexterity(powerStats.getDexterity())
                .agility(powerStats.getAgility())
                .build();
    }

    @Transactional(rollbackFor = {Exception.class})
    public void delete(@NotNull @Positive UUID id) {
        HeroEntity heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        powerStatsService.delete(heroEntity.getPowerStatsId());
        heroRepository.delete(id);
    }

}
