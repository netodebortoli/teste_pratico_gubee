package br.com.gubee.interview.core.features.powerstats;

import java.time.Instant;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.entity.PowerStats;
import br.com.gubee.interview.entity.model.PowerStatsDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    private PowerStatsDTO buildPowerStats(PowerStats powerStatsEntity) {
        return PowerStatsDTO.builder()
                .strength(powerStatsEntity.getStrength())
                .agility(powerStatsEntity.getAgility())
                .dexterity(powerStatsEntity.getDexterity())
                .intelligence(powerStatsEntity.getIntelligence())
                .build();
    }

    @Transactional(rollbackFor = {Exception.class})
    public UUID create(@Valid @NotNull PowerStatsDTO powerStatsDTO) {
        return powerStatsRepository.create(new PowerStats(powerStatsDTO));
    }

    @Transactional(rollbackFor = {Exception.class})
    public PowerStatsDTO update(@NotNull @Positive UUID id, @Valid @NotNull PowerStatsDTO powerStatsDTO) {
        PowerStats powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não encontrado");
        }
        powerStatsEntity.setUpdatedAt(Instant.now());
        powerStatsEntity.setStrength(powerStatsDTO.getStrength());
        powerStatsEntity.setAgility(powerStatsDTO.getAgility());
        powerStatsEntity.setDexterity(powerStatsDTO.getDexterity());
        powerStatsEntity.setIntelligence(powerStatsDTO.getIntelligence());
        powerStatsEntity = powerStatsRepository.update(powerStatsEntity);
        return buildPowerStats(powerStatsEntity);
    }

    public PowerStatsDTO findById(@Positive @NotNull UUID id) {
        PowerStats powerStatsEntity = this.powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não encontrado");
        }
        return buildPowerStats(powerStatsEntity);
    }

    public void delete(@Positive @NotNull UUID id) {
        PowerStats powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID: " + id + " não encontrado");
        }
        powerStatsRepository.delete(id);
    }

}
