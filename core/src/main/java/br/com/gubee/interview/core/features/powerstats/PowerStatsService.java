package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.entity.PowerStatsEntity;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    private PowerStats buildPowerStats(PowerStatsEntity powerStatsEntity) {
        return PowerStats.builder()
                .strength(powerStatsEntity.getStrength())
                .agility(powerStatsEntity.getAgility())
                .dexterity(powerStatsEntity.getDexterity())
                .intelligence(powerStatsEntity.getIntelligence())
                .build();
    }

    @Transactional(rollbackFor = {Exception.class})
    public UUID create(@Valid @NotNull PowerStats powerStatsRequest) {
        return powerStatsRepository.create(new PowerStatsEntity(powerStatsRequest));
    }

    @Transactional(rollbackFor = {Exception.class})
    public PowerStats update(@NotNull @Positive UUID id, @Valid @NotNull PowerStats powerStatsRequest) {
        PowerStatsEntity powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não encontrado");
        }
        powerStatsEntity.setUpdatedAt(LocalDate.now().atStartOfDay().toInstant(ZoneOffset.UTC));
        powerStatsEntity.setStrength(powerStatsRequest.getStrength());
        powerStatsEntity.setAgility(powerStatsRequest.getAgility());
        powerStatsEntity.setDexterity(powerStatsRequest.getDexterity());
        powerStatsEntity.setIntelligence(powerStatsRequest.getIntelligence());
        powerStatsEntity = powerStatsRepository.update(powerStatsEntity);
        return buildPowerStats(powerStatsEntity);
    }

    public PowerStats findById(@Positive @NotNull UUID id) {
        PowerStatsEntity powerStatsEntity = this.powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não encontrado");
        }
        return buildPowerStats(powerStatsEntity);
    }

    public void delete(@Positive @NotNull UUID id) {
        PowerStatsEntity powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null) {
            throw new EntityNotFoundException("PowerStats de ID: " + id + " não encontrado");
        }
        powerStatsRepository.delete(id);
    }

}
