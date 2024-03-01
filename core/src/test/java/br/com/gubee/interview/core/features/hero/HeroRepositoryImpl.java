package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.REVERSE_FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.REVERSE_FLASH_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN_ID;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.gubee.interview.core.features.powerstats.PowerStatsRepositoryImpl;
import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.PowerStats;
import br.com.gubee.interview.entity.model.HeroDTO;

public class HeroRepositoryImpl extends HeroRepository {

   private static Map<UUID, Hero> db;

   private PowerStatsRepositoryImpl powerStatsRepositoryImpl = new PowerStatsRepositoryImpl(null);

   public HeroRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
      super(namedParameterJdbcTemplate);
      initDb();
   }

   private void initDb() {
      db = new HashMap<>();
      BATMAN.setId(BATMAN_ID);
      SUPERMAN.setId(SUPERMAN_ID);
      FLASH.setId(FLASH_ID);
      REVERSE_FLASH.setId(REVERSE_FLASH_ID);
      db.put(BATMAN_ID, BATMAN);
      db.put(FLASH_ID, FLASH);
      db.put(SUPERMAN_ID, SUPERMAN);
      db.put(REVERSE_FLASH_ID, REVERSE_FLASH);
   }

   @Override
   public UUID create(Hero hero) {
      hero.setId(UUID.randomUUID());
      hero.setCreatedAt(Instant.now());
      db.put(hero.getId(), hero);
      return hero.getId();
   }

   @Override
   public Hero findById(UUID id) {
      return db.get(id);
   }

   @Override
   public Hero findByName(String name) {
      return db.values().stream()
            .filter(hero -> hero.getName().toLowerCase().equals(name.toLowerCase()))
            .findFirst()
            .orElse(null);
   }

   @Override
   public boolean delete(UUID id) {
      if (db.containsKey(id)) {
         db.remove(id);
         return true;
      }
      return false;
   }

   @Override
   public boolean update(Hero hero) {
      if (db.containsKey(hero.getId())) {
         hero.setUpdatedAt(Instant.now());
         db.put(hero.getId(), hero);
         return true;
      }
      return false;
   }

   @Override
   public Page<HeroDTO> findAll(Pageable page) {
      List<HeroDTO> list = db.values().stream()
            .map(hero -> {
               PowerStats ps = powerStatsRepositoryImpl.findById(hero.getPowerStatsId());
               return HeroDTO.builder()
                     .name(hero.getName())
                     .race(hero.getRace())
                     .strength(ps.getStrength())
                     .dexterity(ps.getDexterity())
                     .agility(ps.getAgility())
                     .intelligence(ps.getIntelligence())
                     .build();
            })
            .collect(Collectors.toList());

      return new PageImpl<HeroDTO>(list, page, list.size());
   }

   @Override
   public Page<HeroDTO> findAll(String filteredName, Pageable page) {
      List<HeroDTO> list = db.values().stream()
            .filter(hero -> hero.getName().toLowerCase().contains(filteredName.toLowerCase()))
            .map(hero -> {
               PowerStats ps = powerStatsRepositoryImpl.findById(hero.getPowerStatsId());
               return HeroDTO.builder()
                     .name(hero.getName())
                     .race(hero.getRace())
                     .strength(ps.getStrength())
                     .dexterity(ps.getDexterity())
                     .agility(ps.getAgility())
                     .intelligence(ps.getIntelligence())
                     .build();
            })
            .collect(Collectors.toList());

      return new PageImpl<HeroDTO>(list, page, list.size());
   }

}
