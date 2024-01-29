package br.com.gubee.interview.core.features.hero;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PowerStatsDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;
    private final PowerStatsService powerStatsService;

    private HeroDTO buildHero(Hero heroEntity, PowerStatsDTO powerStats) {
        return HeroDTO.builder()
                .name(heroEntity.getName())
                .race(heroEntity.getRace())
                .strength(powerStats.getStrength())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .build();
    }

    private PowerStatsDTO buildPowerStatsFromHero(HeroDTO hero) {
        return PowerStatsDTO.builder()
                .strength(hero.getStrength())
                .agility(hero.getAgility())
                .dexterity(hero.getDexterity())
                .intelligence(hero.getIntelligence())
                .build();
    }

    @Transactional(rollbackFor = { Exception.class })
    public UUID create(@Valid @NotNull HeroDTO heroRequest) {
        UUID powerStatsId = powerStatsService.create(
                buildPowerStatsFromHero(heroRequest));
        return heroRepository.create(new Hero(heroRequest, powerStatsId));
    }

    @Transactional(rollbackFor = { Exception.class })
    public HeroDTO update(@NotNull @Positive UUID id, @Valid @NotNull HeroDTO heroRequest) {
        Hero heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        heroEntity.setRace(heroRequest.getRace());
        heroEntity.setName(heroRequest.getName());
        heroEntity.setUpdatedAt(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
        heroRepository.update(heroEntity);
        PowerStatsDTO powerStats = powerStatsService.update(
                heroEntity.getPowerStatsId(),
                buildPowerStatsFromHero(heroRequest));
        return buildHero(heroEntity, powerStats);
    }

    public HeroDTO findById(@NotNull @Positive UUID id) {
        Hero heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        PowerStatsDTO powerStats = powerStatsService.findById(heroEntity.getPowerStatsId());
        return buildHero(heroEntity, powerStats);
    }

    @Transactional(rollbackFor = { Exception.class })
    public void delete(@NotNull @Positive UUID id) {
        Hero heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        powerStatsService.delete(heroEntity.getPowerStatsId());
        heroRepository.delete(id);
    }

}
