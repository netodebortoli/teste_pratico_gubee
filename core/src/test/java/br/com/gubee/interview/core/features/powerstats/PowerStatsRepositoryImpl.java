package br.com.gubee.interview.core.features.powerstats;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_BATMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_REVERSE_FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_SUPERMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.REVERSE_FLASH_PS_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_PS_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH_PS_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN_PS_ID;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.gubee.interview.entity.PowerStats;

public class PowerStatsRepositoryImpl extends PowerStatsRepository {

   private static Map<UUID, PowerStats> db;

   public PowerStatsRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
      super(namedParameterJdbcTemplate);
      initDb();
   }

   private void initDb() {
      db = new HashMap<>();
      db.put(BATMAN_PS_ID, new PowerStats(POWERSTATS_BATMAN));
      db.put(FLASH_PS_ID, new PowerStats(POWERSTATS_FLASH));
      db.put(SUPERMAN_PS_ID, new PowerStats(POWERSTATS_SUPERMAN));
      db.put(REVERSE_FLASH_PS_ID, new PowerStats(POWERSTATS_REVERSE_FLASH));
   }

   @Override
   public UUID create(PowerStats powerStats) {
      powerStats.setId(UUID.randomUUID());
      powerStats.setCreatedAt(Instant.now());
      db.put(powerStats.getId(), powerStats);
      return powerStats.getId();
   }

   @Override
   public boolean update(PowerStats powerStats) {
      if (db.containsKey(powerStats.getId())) {
         powerStats.setUpdatedAt(Instant.now());
         db.put(powerStats.getId(), powerStats);
         return true;
      }
      return false;
   }

   @Override
   public PowerStats findById(UUID id) {
      return db.get(id);
   }

   @Override
   public boolean delete(UUID id) {
      if (db.containsKey(id)) {
         db.remove(id);
         return true;
      }
      return false;
   }

}
