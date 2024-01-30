package br.com.gubee.interview.core.features.hero;

import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.exception.NegocioException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PowerStatsDTO;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
@Validated
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

    private Hero findHeroById(UUID id) {
        Hero heroEntity = heroRepository.findById(id);
        if (heroEntity == null)
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        return heroEntity;
    }

    public List<HeroDTO> findAll(String filter) {
        if (filter != null && StringUtils.hasText(filter))
            return heroRepository.findAllWithFilterName(filter);
        return heroRepository.findAll(filter);
    }

    @Transactional(rollbackFor = { Exception.class })
    public UUID create(@Valid @NotNull HeroDTO heroRequest) {
        UUID powerStatsId = powerStatsService.create(buildPowerStatsFromHero(heroRequest));
        Hero newHero = new Hero(heroRequest, powerStatsId);
        validateHero(newHero);
        return heroRepository.create(newHero);
    }

    @Transactional(rollbackFor = { Exception.class })
    public HeroDTO update(@NotNull UUID id, @Valid @NotNull HeroDTO heroRequest) {
        Hero heroEntity = findHeroById(id);
        heroEntity.setName(heroRequest.getName());
        heroEntity.setRace(heroRequest.getRace());
        validateHero(heroEntity);
        if (!heroRepository.update(heroEntity))
            throw new NegocioException("Erro ao atualizar Herói de ID: " + id);
        PowerStatsDTO powerStats = powerStatsService.update(
                heroEntity.getPowerStatsId(),
                buildPowerStatsFromHero(heroRequest));
        return buildHero(heroEntity, powerStats);
    }

    public HeroDTO findById(@NotNull UUID id) {
        Hero heroEntity = findHeroById(id);
        PowerStatsDTO powerStats = powerStatsService.findById(heroEntity.getPowerStatsId());
        return buildHero(heroEntity, powerStats);
    }

    @Transactional(rollbackFor = { Exception.class })
    public void delete(@NotNull UUID id) {
        Hero heroEntity = findHeroById(id);
        if (!heroRepository.delete(id))
            throw new NegocioException("Erro ao deletar Herói de ID: " + id);
        powerStatsService.delete(heroEntity.getPowerStatsId());
    }

    public CompareHero compareHeroes(@Valid @NotNull CompareHero compare) {
        return compare;
    }

    private void validateHero(Hero hero) {
        Hero heroFromDB = this.heroRepository.findByName(hero.getName());
        if (hero.getId() == null && heroFromDB != null) {
            throw new NegocioException(
                    String.format("Já existe um herói com o nome %s",
                            heroFromDB.getName().toUpperCase()));
        }
        if (heroFromDB != null && !hero.getId().equals(heroFromDB.getId())) {
            throw new NegocioException(
                    String.format("Já existe um herói com o nome %s",
                            heroFromDB.getName().toUpperCase()));
        }
    }

}
