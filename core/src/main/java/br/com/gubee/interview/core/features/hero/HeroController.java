package br.com.gubee.interview.core.features.hero;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

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
import br.com.gubee.interview.entity.model.PageResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
@Validated
public class HeroController {

    private final HeroService heroService;

    @GetMapping
    public ResponseEntity<PageResponse> findAll(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @Positive @Max(50) int pageSize) {
        PageResponse heroes = heroService.findAll(name, page, pageSize);
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

    @GetMapping(value = "/{id}")
    public ResponseEntity<HeroDTO> findById(@PathVariable @NotNull UUID id) {
        HeroDTO hero = heroService.findById(id);
        return ok().body(hero);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable @NotNull UUID id) {
        heroService.delete(id);
        return noContent().build();
    }

    @GetMapping(value = "/compare")
    public ResponseEntity<CompareHero> compareHero(
            @RequestParam(required = true) @NotNull UUID heroOneId,
            @RequestParam(required = true) @NotNull UUID heroTwoId) {
        CompareHero compare = new CompareHero(heroOneId, heroTwoId);
        compare = heroService.compareHeroes(compare);
        return ok().body(compare);
    }

}
