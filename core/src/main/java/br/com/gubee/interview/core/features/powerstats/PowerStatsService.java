package br.com.gubee.interview.core.features.powerstats;

import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.exception.NegocioException;
import br.com.gubee.interview.entity.PowerStats;
import br.com.gubee.interview.entity.model.PowerStatsDTO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Validated
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

    private PowerStats findPowerStatsById(UUID id) {
        PowerStats powerStatsEntity = powerStatsRepository.findById(id);
        if (powerStatsEntity == null)
            throw new EntityNotFoundException("PowerStats de ID " + id + " não encontrado");
        return powerStatsEntity;
    }

    @Transactional(rollbackFor = { Exception.class })
    public UUID create(@Valid @NotNull PowerStatsDTO powerStatsDTO) {
        return powerStatsRepository.create(new PowerStats(powerStatsDTO));
    }

    @Transactional(rollbackFor = { Exception.class })
    public PowerStatsDTO update(@NotNull UUID id, @Valid @NotNull PowerStatsDTO powerStatsDTO) {
        PowerStats powerStatsEntity = findPowerStatsById(id);
        powerStatsEntity.setStrength(powerStatsDTO.getStrength());
        powerStatsEntity.setAgility(powerStatsDTO.getAgility());
        powerStatsEntity.setDexterity(powerStatsDTO.getDexterity());
        powerStatsEntity.setIntelligence(powerStatsDTO.getIntelligence());
        if (!powerStatsRepository.update(powerStatsEntity))
            throw new NegocioException("Erro ao atualizar PowerStats de ID " + id);
        return buildPowerStats(powerStatsEntity);
    }

    public PowerStatsDTO findById(@NotNull UUID id) {
        PowerStats powerStatsEntity = findPowerStatsById(id);
        return buildPowerStats(powerStatsEntity);
    }

    public void delete(@NotNull UUID id) {
        findPowerStatsById(id);
        if (!powerStatsRepository.delete(id))
            throw new NegocioException("Erro ao deletar PowerStats de ID: " + id);
    }

}
