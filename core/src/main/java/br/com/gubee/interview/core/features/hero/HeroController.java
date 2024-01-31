package br.com.gubee.interview.core.features.hero;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.entity.model.CompareHero;
import br.com.gubee.interview.entity.model.HeroDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
@Validated
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<List<HeroDTO>> findAll(@RequestParam(required = false, name = "filter") String filter) {
        List<HeroDTO> heroes = heroService.findAll(filter);
        return ok().body(heroes);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@RequestBody @Valid HeroDTO hero) {
        final UUID id = heroService.create(hero);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HeroDTO> update(@PathVariable @NotNull UUID id,
            @RequestBody @Valid HeroDTO heroRequest) {
        HeroDTO hero = heroService.update(id, heroRequest);
        return ok().body(hero);
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HeroDTO> findById(@PathVariable @NotNull UUID id) {
        HeroDTO hero = heroService.findById(id);
        return ok().body(hero);
    }

    @DeleteMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable @NotNull UUID id) {
        heroService.delete(id);
        return noContent().build();
    }

    @GetMapping(value = "/compare")
    public ResponseEntity<CompareHero> compareHero(
            @RequestParam(required = true, name = "heroOneId") @NotNull UUID heroOneId,
            @RequestParam(required = true, name = "heroTwoId") @NotNull UUID heroTwoId) {
        CompareHero compare = new CompareHero(heroOneId, heroTwoId);
        compare = heroService.compareHeroes(compare);
        return ok().body(compare);
    }

}
