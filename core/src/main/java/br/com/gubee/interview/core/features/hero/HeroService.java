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
import br.com.gubee.interview.entity.model.ComparedStats;
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

    private PowerStatsDTO buildPowerStatsFromHeroDTO(HeroDTO hero) {
        return PowerStatsDTO.builder()
                .strength(hero.getStrength())
                .agility(hero.getAgility())
                .dexterity(hero.getDexterity())
                .intelligence(hero.getIntelligence())
                .build();
    }

    private Hero findHeroById(UUID id) {
        Hero heroEntity = heroRepository.findById(id);
        if (heroEntity == null) {
            throw new EntityNotFoundException("Herói de ID: " + id + " não encontrado");
        }
        return heroEntity;
    }

    public List<HeroDTO> findAll(String filter) {
        if (filter != null && StringUtils.hasText(filter)) {
            return heroRepository.findAllWithFilterName(filter);
        }
        return heroRepository.findAll(filter);
    }

    @Transactional(rollbackFor = { Exception.class })
    public UUID create(@Valid @NotNull HeroDTO heroRequest) {
        UUID powerStatsId = powerStatsService.create(buildPowerStatsFromHeroDTO(heroRequest));
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
        if (!heroRepository.update(heroEntity)) {
            throw new NegocioException("Erro ao atualizar Herói de ID: " + id);
        }
        PowerStatsDTO powerStats = powerStatsService.update(
                heroEntity.getPowerStatsId(),
                buildPowerStatsFromHeroDTO(heroRequest));
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
        if (!heroRepository.delete(id)) {
            throw new NegocioException("Erro ao deletar Herói de ID: " + id);
        }
        powerStatsService.delete(heroEntity.getPowerStatsId());
    }

    private void validateHero(Hero hero) {
        Hero heroFromDB = this.heroRepository.findByName(hero.getName());
        if (hero.getId() == null && heroFromDB != null) {
            throw new NegocioException(String.format("Já existe um herói com o nome %s",
                    heroFromDB.getName().toUpperCase()));
        }
        if (heroFromDB != null && !hero.getId().equals(heroFromDB.getId())) {
            throw new NegocioException(String.format("Já existe um herói com o nome %s",
                    heroFromDB.getName().toUpperCase()));
        }
    }

    public CompareHero compareHeroes(@Valid @NotNull CompareHero compare) {
        if (compare.getHeroOneId().equals(compare.getHeroTwoId())) {
            throw new NegocioException("Não é possível comparar um herói com ele mesmo");
        }
        HeroDTO heroOne = this.findById(compare.getHeroOneId());
        HeroDTO heroTwo = this.findById(compare.getHeroTwoId());
        compare.setPowerStatsHeroOne(new ComparedStats(buildPowerStatsFromHeroDTO(heroOne)));
        compare.setPowerStatsHeroTwo(new ComparedStats(buildPowerStatsFromHeroDTO(heroTwo)));
        compareStats(compare.getPowerStatsHeroOne(), compare.getPowerStatsHeroTwo());
        return compare;
    }

    private void compareStats(ComparedStats hero1, ComparedStats hero2) {
        compareStat(hero1, hero2, "Strength");
        compareStat(hero1, hero2, "Agility");
        compareStat(hero1, hero2, "Dexterity");
        compareStat(hero1, hero2, "Intelligence");
    }

    private void compareStat(ComparedStats hero1, ComparedStats hero2, String statsName) {
        Long statsHeroOne = Long.parseLong(getStatValue(hero1, statsName));
        Long statsHeroTwo = Long.parseLong(getStatValue(hero2, statsName));
        Long result = statsHeroOne - statsHeroTwo;
        setStatsValue(hero1, statsName, stringBuilderResultCompareStats(getStatValue(hero1, statsName), result));
        setStatsValue(hero2, statsName, stringBuilderResultCompareStats(getStatValue(hero2, statsName), (result * -1)));
    }

    private String getStatValue(ComparedStats hero, String statsName) {
        switch (statsName) {
            case "Strength":
                return hero.getStrength();
            case "Agility":
                return hero.getAgility();
            case "Dexterity":
                return hero.getDexterity();
            case "Intelligence":
                return hero.getIntelligence();
            default:
                throw new IllegalArgumentException("Status inválido: " + statsName);
        }
    }

    private void setStatsValue(ComparedStats hero, String statsName, String resultValue) {
        switch (statsName) {
            case "Strength":
                hero.setStrength(resultValue);
                break;
            case "Agility":
                hero.setAgility(resultValue);
                break;
            case "Dexterity":
                hero.setDexterity(resultValue);
                break;
            case "Intelligence":
                hero.setIntelligence(resultValue);
                break;
            default:
                throw new IllegalArgumentException("Status inválido: " + statsName);
        }
    }

    private String stringBuilderResultCompareStats(String stats, Long result) {
        StringBuilder sb = new StringBuilder();
        sb.append(stats);
        sb.append(" (");
        if (result > 0) {
            sb.append("+");
        } else if (result == 0) {
            return sb.append("=)").toString();
        }
        sb.append(String.valueOf(result));
        sb.append(")");
        return sb.toString();
    }

}
