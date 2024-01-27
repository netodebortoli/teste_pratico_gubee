package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.entity.HeroEntity;
import br.com.gubee.interview.entity.PowerStatsEntity;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional(rollbackFor = {Exception.class})
    public UUID create(@Valid @NotNull PowerStatsEntity powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    public PowerStats findById(@Positive @NotNull UUID id) {
        PowerStatsEntity powerStatsEntity = this.powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não existe");
        }
        return PowerStats.builder()
                .strength(powerStatsEntity.getStrength())
                .agility(powerStatsEntity.getAgility())
                .intelligence(powerStatsEntity.getIntelligence())
                .dexterity(powerStatsEntity.getDexterity())
                .build();
    }

    public void delete(@Positive @NotNull UUID id) {
        PowerStatsEntity powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID: " + id + " não encontrado");
        }
        powerStatsRepository.delete(id);
    }

}
