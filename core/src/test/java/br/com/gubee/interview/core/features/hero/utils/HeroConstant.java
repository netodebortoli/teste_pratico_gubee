package br.com.gubee.interview.core.features.hero.utils;

import java.util.List;
import java.util.UUID;

import br.com.gubee.interview.entity.Hero;
import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;
import br.com.gubee.interview.entity.model.PowerStatsDTO;

public class HeroConstant {
   private HeroConstant() {
   }

   public static UUID RANDOM_HERO_ID = UUID.randomUUID();
   public static UUID RANDOM_POWERSTATS_ID = UUID.randomUUID();

   public static final HeroDTO BATMAN_DTO = new HeroDTO("BATMAN", Race.HUMAN, 8, 8, 9, 10);
   public static final HeroDTO FLASH_DTO = new HeroDTO("FLASH", Race.HUMAN, 10, 10, 7, 8);
   public static final HeroDTO REVERSE_FLASH_DTO = new HeroDTO("REVERSE_FLASH", Race.HUMAN, 10, 10, 6, 9);
   public static final HeroDTO SUPERMAN_DTO = new HeroDTO("SUPERMAN", Race.DIVINE, 10, 9, 9, 9);
   public static final HeroDTO NEW_BATMAN_DTO = new HeroDTO("NEW_BATMAN", Race.DIVINE, 10, 10, 9, 10);
   public static final HeroDTO INVALID_HER0 = new HeroDTO("", null, null, null, null, null);

   public static final PowerStatsDTO POWERSTATS_BATMAN = new PowerStatsDTO(8, 8, 9, 10);
   public static final PowerStatsDTO POWERSTATS_FLASH = new PowerStatsDTO(10, 10, 7, 8);
   public static final PowerStatsDTO POWERSTATS_REVERSE_FLASH = new PowerStatsDTO(10, 10, 6, 9);
   public static final PowerStatsDTO POWERSTATS_SUPERMAN = new PowerStatsDTO(10, 9, 9, 9);

   public static UUID BATMAN_PS_ID = UUID.randomUUID();
   public static UUID FLASH_PS_ID = UUID.randomUUID();
   public static UUID REVERSE_FLASH_PS_ID = UUID.randomUUID();
   public static UUID SUPERMAN_PS_ID = UUID.randomUUID();

   public static UUID BATMAN_ID = UUID.randomUUID();
   public static UUID FLASH_ID = UUID.randomUUID();
   public static UUID REVERSE_FLASH_ID = UUID.randomUUID();
   public static UUID SUPERMAN_ID = UUID.randomUUID();

   public static final Hero BATMAN = new Hero(BATMAN_DTO, BATMAN_PS_ID);
   public static final Hero FLASH = new Hero(FLASH_DTO, FLASH_PS_ID);
   public static final Hero REVERSE_FLASH = new Hero(REVERSE_FLASH_DTO, REVERSE_FLASH_PS_ID);
   public static final Hero SUPERMAN = new Hero(SUPERMAN_DTO, SUPERMAN_PS_ID);

   public static final PageResponse HEROES_DTO = new PageResponse(
         List.of(BATMAN_DTO, SUPERMAN_DTO, FLASH_DTO),
         1L,
         3L,
         0L);

   public static final CompareHero COMPARE_HERO = new CompareHero(BATMAN_ID, FLASH_ID);
   public static final CompareHero INVALID_COMPARE = new CompareHero(BATMAN_ID, BATMAN_ID);
}
