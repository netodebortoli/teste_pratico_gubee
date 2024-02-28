package br.com.gubee.interview.core.features.hero;

import static br.com.gubee.interview.core.features.hero.HeroConstant.BATMAN;
import static br.com.gubee.interview.core.features.hero.HeroConstant.HEROES;
import static br.com.gubee.interview.core.features.hero.HeroConstant.NEW_BATMAN;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.gubee.interview.core.exception.EntityNotFoundException;
import br.com.gubee.interview.core.exception.NegocioException;
import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import br.com.gubee.interview.entity.model.PageResponse;

@WebMvcTest(HeroController.class)
class HeroControllerTest {

   @Autowired
   private MockMvc mockMvc;

   @Autowired
   private ObjectMapper objectMapper;

   @MockBean
   private HeroService heroService;

   private String uriRequest = "/api/v1/heroes";

   @Test
   void createHero_WithValidData_ReturnsCreated() throws Exception {
      when(heroService.create(BATMAN)).thenReturn(UUID.randomUUID());

      mockMvc.perform(
            post(uriRequest)
                  .content(objectMapper.writeValueAsString(BATMAN))
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("Location"));
   }

   @Test
   void createHero_WithInvalidHero_ReturnsBadRequest() throws Exception {
      HeroDTO invalidHero = new HeroDTO("", null, null, null, null, null);

      mockMvc.perform(
            post(uriRequest)
                  .content(objectMapper.writeValueAsString(invalidHero))
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
   }

   @Test
   void updateHero_WithExistingId_ReturnsUpdatedHero() throws Exception {
      UUID id = UUID.randomUUID();
      when(heroService.update(id, BATMAN)).thenReturn(NEW_BATMAN);

      mockMvc.perform(
            put(uriRequest + "/{id}", id)
                  .content(objectMapper.writeValueAsString(BATMAN))
                  .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(NEW_BATMAN.getName()))
            .andExpect(jsonPath("$.strength").value(NEW_BATMAN.getStrength()))
            .andExpect(jsonPath("$.intelligence").value(NEW_BATMAN.getIntelligence()));

   }

   @Test
   void getHero_WithExistingId_ReturnsHero() throws Exception {
      UUID id = UUID.randomUUID();
      when(heroService.findById(id)).thenReturn(BATMAN);

      mockMvc.perform(
            get(uriRequest + "/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value(BATMAN.getName()));
   }

   @Test
   void getHero_WithUnexistingId_ReturnsNotFound() throws Exception {
      UUID id = UUID.randomUUID();
      when(heroService.findById(id)).thenThrow(EntityNotFoundException.class);
      mockMvc.perform(
            get(uriRequest + "/{id}", id))
            .andExpect(status().isNotFound());
   }

   // Com problema
   @Test
   public void listHero_ReturnsAllHeros() throws Exception {
      when(heroService.findAll(
            anyString(), anyInt(), anyInt()))
            .thenReturn(HEROES);

      mockMvc
            .perform(
                  get(uriRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", hasSize(3)));
   }

   // Com problema
   @Test
   public void listHero_ReturnsNoHeros() throws Exception {
      when(heroService.findAll(
            anyString(), anyInt(), anyInt()))
            .thenReturn(new PageResponse(List.of(), null, null, null));

      mockMvc
            .perform(
                  get(uriRequest))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.result", hasSize(0)));
   }

   @Test
   public void deleteHero_WithExistingId_ReturnsNoContent() throws Exception {
      mockMvc.perform(
            delete(uriRequest + "/{id}", UUID.randomUUID()))
            .andExpect(status().isNoContent());
   }

   @Test
   public void deleteHero_WithUnexistingId_ReturnsNoFound() throws Exception {
      UUID id = UUID.randomUUID();
      doThrow(EntityNotFoundException.class).when(heroService).delete(id);
      mockMvc.perform(
            delete(uriRequest + "/{id}", id))
            .andExpect(status().isNotFound());
   }

   @Test
   public void compareHero_WithValidHeroes_ReturnsCompare() throws Exception {
      UUID heroOneId = UUID.randomUUID();
      UUID heroTwoId = UUID.randomUUID();
      CompareHero compare = new CompareHero(heroOneId, heroTwoId);

      when(heroService.compareHeroes(any())).thenReturn(compare);

      mockMvc.perform(
            get(uriRequest + "/compare")
                  .param("heroOneId", heroOneId.toString())
                  .param("heroTwoId", heroTwoId.toString()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.hero_one_id").value(heroOneId.toString()))
            .andExpect(jsonPath("$.hero_two_id").value(heroTwoId.toString()));
   }

   @Test
   public void compareHero_WithSameHeroes_ReturnsBadRequest() throws Exception {
      UUID heroOneId = UUID.randomUUID();
      UUID heroTwoId = heroOneId;

      when(heroService.compareHeroes(any())).thenThrow(NegocioException.class);

      mockMvc.perform(
            get(uriRequest + "/compare")
                  .param("heroOneId", heroOneId.toString())
                  .param("heroTwoId", heroTwoId.toString()))
            .andExpect(status().isBadRequest());
   }

   @Test
   public void compareHero_WithUnexestingId_ReturnsNotFound() throws Exception {

      when(heroService.compareHeroes(any())).thenThrow(EntityNotFoundException.class);

      mockMvc.perform(
            get(uriRequest + "/compare")
                  .param("heroOneId", UUID.randomUUID().toString())
                  .param("heroTwoId", UUID.randomUUID().toString()))
            .andExpect(status().isNotFound());
   }

}