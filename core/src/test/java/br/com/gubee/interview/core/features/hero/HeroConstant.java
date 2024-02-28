package br.com.gubee.interview.core.features.hero;

import java.util.List;

import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;

public class HeroConstant {
   public static final HeroDTO BATMAN = new HeroDTO("BATMAN", Race.HUMAN, 8, 8, 9, 10);
   public static final HeroDTO FLASH = new HeroDTO("FLASH", Race.HUMAN, 10, 10, 7, 8);
   public static final HeroDTO SUPER_MAN = new HeroDTO("SUPERMAN", Race.DIVINE, 10, 9, 9, 9);
   public static final HeroDTO NEW_BATMAN = new HeroDTO("NEW_BATMAN", Race.HUMAN, 10, 10, 9, 10);
   public static final PageResponse HEROES = new PageResponse(List.of(BATMAN, SUPER_MAN, FLASH), 1L, 3L, 0L);
}
