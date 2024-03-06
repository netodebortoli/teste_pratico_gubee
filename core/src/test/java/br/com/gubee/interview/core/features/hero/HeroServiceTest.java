package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.NEW_BATMAN_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.RANDOM_HERO_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.exception.NegocioException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.core.features.powerstats.PowerStatsServiceImpl;
import br.com.gubee.interview.entity.enums.Race;
import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;

public class HeroServiceTest {

   HeroService heroService;

   PowerStatsService powerStatsService;

   HeroRepositoryImpl heroRepository;

   @BeforeEach
   void setup() {
      heroRepository = new HeroRepositoryImpl(null);
      powerStatsService = new PowerStatsServiceImpl(null);
      heroService = new HeroService(heroRepository, powerStatsService);
   }

   @Test
   void shouldCreateNewHero() {
      // given
      HeroDTO newHero = NEW_BATMAN_DTO;

      // when
      UUID sut = heroService.create(newHero);

      // then
      assertThat(sut).isNotNull();
      assertThat(sut).isInstanceOf(UUID.class);
   }

   @Test
   void shouldThrowsException_WhenCreateHero_WithAlreadyNameExists() {
      // given
      HeroDTO batman = NEW_BATMAN_DTO;

      // when
      heroService.create(batman);

      // then
      assertThatThrownBy(() -> heroService.create(batman))
            .isInstanceOf(NegocioException.class)
            .hasMessage("Já existe um herói com o nome " + batman.getName());
   }

   @Test
   void shouldUpdatHeroAndReturnsUpdatedHero() {
      // given
      HeroDTO batman = BATMAN_DTO;
      batman.setName("DIVINE BATMAN");
      batman.setRace(Race.DIVINE);

      // when
      HeroDTO sut = heroService.update(BATMAN_ID, batman);

      // then
      assertThat(sut).isNotNull();
      assertThat(sut.getName()).isEqualTo(batman.getName());
      assertThat(sut.getRace()).isEqualTo(batman.getRace());
   }

   @Test
   void shouldThrowsException_WhenUpdateHero_WithInvalidId() {
      // given
      HeroDTO hero = BATMAN_DTO;
      UUID id = RANDOM_HERO_ID;

      // then
      assertThatThrownBy(() -> heroService.update(id, hero))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Herói de ID: " + id + " não encontrado");
   }

   @Test
   void shouldFindHero_WithValidId() {
      // given
      UUID idSuperman = SUPERMAN_ID;

      // when
      HeroDTO sut = heroService.findById(idSuperman);

      // then
      assertThat(sut).isNotNull();
      assertThat(SUPERMAN_DTO.getName()).isEqualTo(sut.getName());
      assertThat(SUPERMAN_DTO.getRace().name()).isEqualTo(sut.getRace().name());
   }

   @Test
   void shouldThrowsException_WhenFindUnexistingHero() {
      // given
      UUID id = RANDOM_HERO_ID;

      // then
      assertThatThrownBy(() -> heroService.findById(id))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Herói de ID: " + id + " não encontrado");
   }

   @Test
   void shouldDeleteHero() {
      // given
      UUID idSuperman = SUPERMAN_ID;

      // when
      heroService.delete(idSuperman);

      // then
      assertThatCode(() -> heroService.findById(idSuperman)).isInstanceOfAny(EntityNotFoundException.class);
   }

   @Test
   void shouldThrowsException_WhenDeleteUnexstingHero() {
      // given
      UUID id = RANDOM_HERO_ID;

      // then
      assertThatThrownBy(() -> heroService.delete(id))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Herói de ID: " + id + " não encontrado");
   }

   @Test
   void shouldListAllHeroes() {
      // when
      PageResponse sut = heroService.findAll(null, 0, 5);

      // then
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(4);
   }

   @Test
   void shouldListFilteredHeroes() {
      // given
      String heroName = "FLASH";

      // when
      PageResponse sut = heroService.findAll(heroName, 0, 5);

      // then
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(2);
      assertThat(sut.getResult().get(0).getName()).contains(heroName);
      assertThat(sut.getResult().get(1).getName()).contains(heroName);
   }

   @Test
   void shouldReturnsEmptyList_WithUnesxtingHero() {
      // given
      String heroName = "AQUAMAN";

      // when
      PageResponse sut = heroService.findAll(heroName, 0, 5);

      // then
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(0);
   }

   @Test
   void shouldReturnsCompareHeroes_WithValidHeroes() {
      // given
      CompareHero compareHero = new CompareHero(FLASH_ID, SUPERMAN_ID);

      // when
      CompareHero sut = heroService.compareHeroes(compareHero);

      // then
      assertThat(sut).isNotNull();
      assertThat(sut.getHeroOneId()).isEqualTo(FLASH_ID);
      assertThat(sut.getPowerStatsHeroOne()).isNotNull();
      assertThat(sut.getHeroTwoId()).isEqualTo(SUPERMAN_ID);
      assertThat(sut.getPowerStatsHeroTwo()).isNotNull();
   }

   @Test
   void shouldThrowsException_WhenCompareSameHeroes() {
      // given
      CompareHero compareHero = new CompareHero(SUPERMAN_ID, SUPERMAN_ID);

      // then
      assertThatThrownBy(() -> heroService.compareHeroes(compareHero))
            .hasMessageContaining("Não é possível comparar um herói com ele mesmo")
            .isInstanceOf(NegocioException.class);
   }

   @Test
   void shouldThrowsException_WhenCompareUnexistingHeroes() {
      // given
      CompareHero compareHero = new CompareHero(UUID.randomUUID(), UUID.randomUUID());

      // then
      assertThatThrownBy(() -> heroService.compareHeroes(compareHero))
            .isInstanceOf(EntityNotFoundException.class);
   }

}
