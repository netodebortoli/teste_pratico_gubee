package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.heroId;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import br.com.gubee.interview.entity.Hero;

public class HeroRepositoryImpl extends HeroRepository {

   private static List<Hero> db;

   public HeroRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
      super(namedParameterJdbcTemplate);
      db = new ArrayList<>();
   }

   @Override
   public UUID create(Hero hero) {
      hero.setId(heroId);
      db.add(hero);
      return hero.getId();
   }

   @Override
   public Hero findById(UUID id) {
      Hero hero = BATMAN;
      hero.setId(id);
      return hero;
   }

   @Override
   public Hero findByName(String name) {
      return BATMAN;
   }

   @Override
   public boolean delete(UUID id) {
      return true;
   }

   @Override
   public boolean update(Hero hero) {
      return true;
   }

}
