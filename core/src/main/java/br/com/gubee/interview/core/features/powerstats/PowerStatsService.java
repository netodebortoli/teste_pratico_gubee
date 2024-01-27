package br.com.gubee.interview.core.features.powerstats;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.entity.PowerStatsEntity;
import br.com.gubee.interview.entity.model.PowerStats;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PowerStatsService {

    private final PowerStatsRepository powerStatsRepository;

    @Transactional
    public UUID create(PowerStatsEntity powerStats) {
        return powerStatsRepository.create(powerStats);
    }

    public PowerStats findById(UUID id) {
        PowerStats powerStats = this.powerStatsRepository.findById(id);
        if (powerStats == null) {
            throw new EntityNotFoundException("PowerStats de ID " + id + " não existe");
        }
        return powerStats;
    }

}
