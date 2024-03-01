package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_ID;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.COMPARE_HERO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.INVALID_COMPARE;
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
   void createHero_WithValidData_ReturnsUUID() {
      UUID sut = heroService.create(NEW_BATMAN_DTO);
      assertThat(sut).isNotNull();
      assertThat(sut).isInstanceOf(UUID.class);
   }

   @Test
   void createHero_WithAlreadyNameExists_ThrowException() {
      assertThatThrownBy(() -> heroService.create(SUPERMAN_DTO))
            .isInstanceOf(NegocioException.class)
            .hasMessage("Já existe um herói com o nome " + SUPERMAN_DTO.getName());
   }

   @Test
   void updateHero_WithValidData_ReturnsUpdatedHero() {
      HeroDTO sut = heroService.update(BATMAN_ID, NEW_BATMAN_DTO);
      assertThat(sut).isNotNull();
      assertThat(sut.getName()).isEqualTo(NEW_BATMAN_DTO.getName());
      assertThat(sut.getRace()).isEqualTo(NEW_BATMAN_DTO.getRace());
   }

   @Test
   void updateHero_WithInvalidId_ThrowsException() {
      assertThatThrownBy(() -> heroService.update(RANDOM_HERO_ID, NEW_BATMAN_DTO))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Herói de ID: " + RANDOM_HERO_ID + " não encontrado");
   }

   @Test
   void findHero_WithValidId_ReturnsHeroDTO() {
      HeroDTO sut = heroService.findById(SUPERMAN_ID);
      assertThat(sut).isNotNull();
      assertThat(SUPERMAN_DTO.getName()).isEqualTo(sut.getName());
      assertThat(SUPERMAN_DTO.getRace().name()).isEqualTo(sut.getRace().name());
   }

   @Test
   void findHero_WithInvalidId_ReturnsException() {
      assertThatThrownBy(() -> heroService.findById(RANDOM_HERO_ID))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Herói de ID: " + RANDOM_HERO_ID + " não encontrado");
   }

   @Test
   void deleteHero_WithValidId_DoesNotThrowAnyExpcetion() {
      assertThatCode(() -> heroService.delete(SUPERMAN_ID)).doesNotThrowAnyException();
   }

   @Test
   void deleteHero_WithInvalidId_ThrowException() {
      assertThatThrownBy(() -> heroService.delete(RANDOM_HERO_ID))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessageContaining("Herói de ID: " + RANDOM_HERO_ID + " não encontrado");
   }

   @Test
   void listAllHeroes_ReturnsHeroes() {
      PageResponse sut = heroService.findAll(null, 0, 5);
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(4);
   }

   @Test
   void listAllHeroes_WithFilteredName_ReturnsFilteredHeroes() {
      String heroName = "FLASH";
      PageResponse sut = heroService.findAll(heroName, 0, 5);
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(2);
      assertThat(sut.getResult().get(0).getName()).contains(heroName);
      assertThat(sut.getResult().get(1).getName()).contains(heroName);
   }

   @Test
   void listHeroes_WithUnesxtingHero_ReturnsEmptyList() {
      String heroName = "AQUAMAN";
      PageResponse sut = heroService.findAll(heroName, 0, 5);
      assertThat(sut.getResult()).isNotNull();
      assertThat(sut.getResult()).hasSize(0);
   }

   @Test
   void compareHeroes_WithValidHeroes_ReturnsComparedHeroes() {
      CompareHero sut = heroService.compareHeroes(COMPARE_HERO);
      assertThat(sut).isNotNull();
      assertThat(sut.getPowerStatsHeroOne()).isNotNull();
      assertThat(sut.getPowerStatsHeroTwo()).isNotNull();
   }

   @Test
   void compareHeroes_WithSameHeroes_ThrowException() {
      assertThatThrownBy(() -> heroService.compareHeroes(INVALID_COMPARE))
            .hasMessageContaining("Não é possível comparar um herói com ele mesmo")
            .isInstanceOf(NegocioException.class);
   }

   @Test
   void compareHeroes_InvalidHeroes_ThrowException() {
      assertThatThrownBy(() -> heroService.compareHeroes(new CompareHero(UUID.randomUUID(), UUID.randomUUID())))
            .isInstanceOf(EntityNotFoundException.class);
   }

}
