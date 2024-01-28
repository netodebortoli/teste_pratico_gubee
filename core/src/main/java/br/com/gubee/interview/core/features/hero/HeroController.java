package br.com.gubee.interview.core.features.hero;

import br.com.gubee.interview.entity.model.Hero;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated @RequestBody Hero hero) {
        final UUID id = heroService.create(hero);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Hero> update(@PathVariable @NotNull @Positive UUID id,
           @NotNull @Validated @RequestBody Hero heroRequest) {
        Hero hero = heroService.update(id, heroRequest);
        return ok().body(hero);
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Hero> findById(@PathVariable @NotNull @Positive UUID id) {
        Hero hero = heroService.findById(id);
        return ok().body(hero);
    }

    @DeleteMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive UUID id) {
        heroService.delete(id);
        return noContent().build();
    }

}
