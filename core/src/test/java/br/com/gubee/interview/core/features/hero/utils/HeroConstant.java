package br.com.gubee.interview.core.features.hero.utils;

import java.util.List;
import java.util.UUID;

import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;
import br.com.gubee.interview.entity.model.PowerStatsDTO;

public class HeroConstant {
   public static UUID heroId = UUID.randomUUID();
   public static UUID psId = UUID.randomUUID();

   public static final HeroDTO BATMAN_DTO = new HeroDTO("BATMAN", Race.HUMAN, 8, 8, 9, 10);
   public static final HeroDTO FLASH_DTO = new HeroDTO("FLASH", Race.HUMAN, 10, 10, 7, 8);
   public static final HeroDTO SUPERMAN_DTO = new HeroDTO("SUPERMAN", Race.DIVINE, 10, 9, 9, 9);
   public static final HeroDTO NEW_BATMAN_DTO = new HeroDTO("NEW_BATMAN", Race.HUMAN, 10, 10, 9, 10);

   public static final PowerStatsDTO POWERSTATS_BATMAN = new PowerStatsDTO(8, 8, 9, 10);
   public static final PowerStatsDTO POWERSTATS_FLASH = new PowerStatsDTO(10, 10, 7, 8);

   public static final Hero BATMAN = new Hero(BATMAN_DTO, psId);
   public static final Hero FLASH = new Hero(FLASH_DTO, psId);
   public static final Hero SUPERMAN = new Hero(SUPERMAN_DTO, psId);

   public static final PageResponse HEROES_DTO = new PageResponse(
         List.of(BATMAN_DTO, SUPERMAN_DTO, FLASH_DTO),
         1L,
         3L,
         0L);

}
