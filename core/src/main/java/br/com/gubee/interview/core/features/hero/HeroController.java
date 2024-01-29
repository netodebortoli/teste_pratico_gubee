package br.com.gubee.interview.core.features.hero;

import static java.lang.String.format;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

import java.net.URI;
import java.util.UUID;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.gubee.interview.entity.model.HeroDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/heroes", produces = APPLICATION_JSON_VALUE)
public class HeroController {

    private final HeroService heroService;

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> create(@Validated @RequestBody HeroDTO hero) {
        final UUID id = heroService.create(hero);
        return created(URI.create(format("/api/v1/heroes/%s", id))).build();
    }

    @PutMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HeroDTO> update(@PathVariable @NotNull @Positive UUID id,
           @NotNull @Validated @RequestBody HeroDTO heroRequest) {
        HeroDTO hero = heroService.update(id, heroRequest);
        return ok().body(hero);
    }

    @GetMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<HeroDTO> findById(@PathVariable @NotNull @Positive UUID id) {
        HeroDTO hero = heroService.findById(id);
        return ok().body(hero);
    }

    @DeleteMapping(value = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> delete(@PathVariable @NotNull @Positive UUID id) {
        heroService.delete(id);
        return noContent().build();
    }

}
