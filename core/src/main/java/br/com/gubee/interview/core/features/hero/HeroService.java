package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.model.Hero;
import br.com.gubee.interview.model.PowerStats;
import br.com.gubee.interview.model.request.CreateHeroRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@AllArgsConstructor
public class HeroService {

    private final HeroRepository heroRepository;

    @Transactional
    public UUID create(CreateHeroRequest createHeroRequest) {
        return heroRepository.create(new Hero(createHeroRequest, null));
    }

}
