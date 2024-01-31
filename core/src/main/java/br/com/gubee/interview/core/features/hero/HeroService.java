package br.com.gubee.interview.core.features.hero;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
import br.com.gubee.interview.entity.model.PageResponse;
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

    public PageResponse findAll(String filter,
            @PositiveOrZero int page, @Positive @Max(100) int pageSize) {
        Page<HeroDTO> pageHeroes;
        if (filter != null && StringUtils.hasText(filter)) {
            pageHeroes = heroRepository.findAll(filter, PageRequest.of(page, pageSize));
        } else {
            pageHeroes = heroRepository.findAll(PageRequest.of(page, pageSize));
        }
        return PageResponse.builder()
                .result(pageHeroes.getContent())
                .totalElements(pageHeroes.getTotalElements())
                .totalPages(Long.valueOf(pageHeroes.getTotalPages()))
                .currentPage(Long.valueOf(pageHeroes.getNumber()))
                .build();
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
        compareStat(hero1, hero2, ComparedStats.STRENGHT);
        compareStat(hero1, hero2, ComparedStats.AGILITY);
        compareStat(hero1, hero2, ComparedStats.DEXTERITY);
        compareStat(hero1, hero2, ComparedStats.INTELLIGENCE);
    }

    private void compareStat(ComparedStats hero1, ComparedStats hero2, int statsName) {
        Long statsHeroOne = Long.parseLong(hero1.getStatsValue(statsName));
        Long statsHeroTwo = Long.parseLong(hero2.getStatsValue(statsName));
        Long result = statsHeroOne - statsHeroTwo;
        hero1.setStatsValue(statsName, buildResultComparedStats(hero1.getStatsValue(statsName), result));
        hero2.setStatsValue(statsName, buildResultComparedStats(hero2.getStatsValue(statsName), (result * -1)));
    }

    private String buildResultComparedStats(String stats, Long result) {
        StringBuilder sb = new StringBuilder();
        sb.append(stats).append(" ");
        sb.append("(");
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
