package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.FLASH_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_BATMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.POWERSTATS_FLASH;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.SUPERMAN;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.heroId;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.psId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.exception.NegocioException;
import br.com.gubee.interview.core.features.powerstats.PowerStatsService;
import br.com.gubee.interview.entity.model.HeroDTO;

@ExtendWith(MockitoExtension.class)
public class HeroServiceTest {

   @InjectMocks
   HeroService heroService;
   @Mock
   PowerStatsService powerStatsService;
   @Mock
   HeroRepositoryImpl heroRepository;

   @Test
   void createHero_WithValidData_ReturnsUUID() {
      when(powerStatsService.create(POWERSTATS_BATMAN)).thenReturn(psId);
      when(heroRepository.findByName(BATMAN_DTO.getName())).thenReturn(null);
      when(heroRepository.create(BATMAN)).thenReturn(heroId);

      UUID sut = heroService.create(BATMAN_DTO);

      assertThat(sut).isNotNull();
      assertThat(sut).isEqualTo(heroId);
   }

   @Test
   void createHero_WithAlreadyNameExists_ThrowException() {
      when(heroRepository.findByName(BATMAN_DTO.getName())).thenReturn(BATMAN);

      assertThatThrownBy(() -> heroService.create(BATMAN_DTO))
            .isInstanceOf(NegocioException.class)
            .hasMessage("Já existe um herói com o nome " + BATMAN_DTO.getName());
   }

   @Test
   void findHero_WithValidId_ReturnsHeroDTO() {
      when(heroRepository.findById(heroId)).thenReturn(FLASH);
      when(powerStatsService.findById(psId)).thenReturn(POWERSTATS_FLASH);

      HeroDTO sut = heroService.findById(heroId);

      assertThat(sut).isNotNull();
      assertThat(FLASH_DTO).isEqualTo(sut);
   }

   @Test
   void findHero_WithInvaidId_ReturnsException() {
      UUID id = UUID.randomUUID();
      when(heroRepository.findById(id)).thenReturn(null);

      assertThatThrownBy(() -> heroService.findById(id))
            .isInstanceOf(EntityNotFoundException.class)
            .hasMessage("Herói de ID: " + id + " não encontrado");
   }

   @Test
   void deleteHero_WithValidId_doesNotThrowAnyException() {
      when(heroRepository.findById(heroId)).thenReturn(SUPERMAN);
      when(heroRepository.delete(heroId)).thenReturn(true);
      assertThatCode(() -> heroService.delete(heroId)).doesNotThrowAnyException();
   }

   @Test
   void noDeleteHero_WhenDeleteReturnsFalse_ThrowNegocioException() {
      UUID id = UUID.randomUUID();
      when(heroRepository.findById(any())).thenReturn(SUPERMAN);
      when(heroRepository.delete(id)).thenReturn(false);
      assertThatThrownBy(() -> heroService.delete(id))
            .isInstanceOf(NegocioException.class)
            .hasMessageContaining("Erro ao deletar Herói de ID: " + id);
   }

}
