package br.com.gubee.interview.core.features.hero;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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

    public List<HeroDTO> findAll(String filter) {
        if (filter != null && StringUtils.hasText(filter)) {
            return heroRepository.findAllWithFilterName(filter);
        }
        return heroRepository.findAll(filter);  
    }

    @Transactional(rollbackFor = { Exception.class })
    public UUID create(@Valid @NotNull HeroDTO heroRequest) {
        UUID powerStatsId = powerStatsService.create(buildPowerStatsFromHero(heroRequest));
        validateHero(heroRequest);
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
        heroEntity.setUpdatedAt(Instant.now());
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
        heroRepository.delete(id);
        powerStatsService.delete(heroEntity.getPowerStatsId());
    }

    private void validateHero(HeroDTO heroRequest) {
        if (heroRepository.findByNameIfExists(heroRequest.getName())) {
            throw new IllegalArgumentException("Herói de nome: " + heroRequest.getName() + " já existe");
        }
    }

}
