package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.BATMAN_DTO;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.INVALID_HER0;
import static br.com.gubee.interview.core.features.hero.utils.HeroConstant.NEW_BATMAN_DTO;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@Sql(scripts = "/clean_tables.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = "/populate_heroes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class HeroServiceIT {

   @Autowired
   private TestRestTemplate testRestTemplate;

   @Test
   void createHero_WithValidData_ReturnsStatusCreated() {
      ResponseEntity<Void> sut = testRestTemplate.postForEntity(
            "/api/v1/heroes",
            NEW_BATMAN_DTO,
            Void.class,
            MediaType.APPLICATION_JSON);

      assertThat(sut).isNotNull();
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.CREATED);
      assertThat(sut.getHeaders().getLocation()).isNotNull();
   }

   @Test
   void createHero_WithInvalidData_ReturnsBadRequest() {
      ResponseEntity<Void> sut = testRestTemplate.postForEntity(
            "/api/v1/heroes",
            INVALID_HER0,
            Void.class,
            MediaType.APPLICATION_JSON);

      assertThat(sut).isNotNull();
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
   }

   @Test
   void updateHero_FindHeroUpdated() {
      UUID batmanId = UUID.fromString("c8a70a49-b174-4466-a35d-6f8a8a6fb243");

      testRestTemplate.put(
            "/api/v1/heroes/{id}",
            NEW_BATMAN_DTO,
            batmanId);

      ResponseEntity<HeroDTO> sut = testRestTemplate.getForEntity(
            "/api/v1/heroes/{id}",
            HeroDTO.class,
            batmanId);

      assertThat(sut.getBody()).isNotNull();
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(NEW_BATMAN_DTO).isEqualTo(sut.getBody());
   }

   @Test
   void listHeroes_WithoutAndWithFilter_ReturnsPageHeroes() {
      ResponseEntity<PageResponse> sut = testRestTemplate.getForEntity(
            "/api/v1/heroes",
            PageResponse.class);

      assertThat(sut.getBody()).isNotNull();
      assertThat(sut.getBody().getResult()).hasSize(3);
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);

      sut = testRestTemplate.getForEntity(
            "/api/v1/heroes?name={name}",
            PageResponse.class,
            "BATMAN");

      assertThat(sut.getBody()).isNotNull();
      assertThat(sut.getBody().getResult()).hasSize(1);
      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(sut.getBody().getResult().get(0)).isEqualTo(BATMAN_DTO);
   }

   @Test
   void removeHero_ReturnsNoContent() {
      UUID id = UUID.fromString("c8a70a49-b174-4466-a35d-6f8a8a6fb243");

      ResponseEntity<Void> sut = testRestTemplate.exchange(
            "/api/v1/heroes/{id}",
            HttpMethod.DELETE,
            null,
            Void.class,
            id);

      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
   }

   @Test
   void getCompareHeroes_ReturnsComparedHeroes() {
      UUID heroOneId = UUID.fromString("c8a70a49-b174-4466-a35d-6f8a8a6fb243");
      UUID heroTwoId = UUID.fromString("1f117d30-a8a0-4735-a5f5-3759072bf499");

      ResponseEntity<CompareHero> sut = testRestTemplate.getForEntity(
            "/api/v1/heroes/compare?heroOneId={heroOneId}&heroTwoId={heroTwoId}",
            CompareHero.class,
            heroOneId, heroTwoId);

      assertThat(sut.getStatusCode()).isEqualTo(HttpStatus.OK);
      assertThat(sut.getBody().getHeroOneId()).isEqualTo(heroOneId);
      assertThat(sut.getBody().getHeroTwoId()).isEqualTo(heroTwoId);
   }

}
