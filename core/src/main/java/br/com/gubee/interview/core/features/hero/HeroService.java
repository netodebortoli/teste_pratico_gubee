package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.entity.HeroEntity;
import br.com.gubee.interview.entity.model.Hero;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@AllArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final PowerStatsService powerStatsService;

    private Hero buildHero(HeroEntity heroEntity, PowerStats powerStats) {
        return Hero.builder()
                .name(heroEntity.getName())
                .race(heroEntity.getRace())
                .strength(powerStats.getStrength())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .build();
    }

    private PowerStats buildPowerStatsFromHero(Hero hero) {
        return PowerStats.builder()
                .strength(hero.getStrength())
                .agility(hero.getAgility())
                .dexterity(hero.getDexterity())
                .intelligence(hero.getIntelligence())
                .build();
    }

    @Transactional(rollbackFor = {Exception.class})
    public UUID create(@Valid @NotNull Hero heroRequest) {
        UUID powerStatsId = powerStatsService.create(
                buildPowerStatsFromHero(heroRequest));
        return heroRepository.create(new HeroEntity(heroRequest, powerStatsId));
    }

    @Transactional(rollbackFor = {Exception.class})
    public Hero update(@NotNull @Positive UUID id, @Valid @NotNull Hero heroRequest) {
        HeroEntity heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        heroEntity.setRace(heroRequest.getRace());
        heroEntity.setName(heroRequest.getName());
        heroEntity.setUpdatedAt(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
        heroRepository.update(heroEntity);
        PowerStats powerStats = powerStatsService.update(
                heroEntity.getPowerStatsId(),
                buildPowerStatsFromHero(heroRequest));
        return buildHero(heroEntity, powerStats);
    }

    public Hero findById(@NotNull @Positive UUID id) {
        HeroEntity heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        PowerStats powerStats = powerStatsService.findById(heroEntity.getPowerStatsId());
        return buildHero(heroEntity, powerStats);
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
