package com.backend.toh.controllers;

import com.backend.toh.domain.Hero;
import com.backend.toh.services.HeroService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping(value = "/api/heroes", produces = MediaType.APPLICATION_JSON_VALUE)
public class HeroesController {

    private final HeroService heroService;

    public HeroesController(HeroService heroService) {
        this.heroService = heroService;
    }

    @GetMapping()
    public Collection<Hero> getHeroes() {
        return heroService.getAllHeroes();
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Hero> getHero(@PathVariable int id) {
        return heroService.getHeroById(id)
                          .map(hero -> new ResponseEntity<>(hero, HttpStatus.OK))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping()
    public ResponseEntity<Hero> addHero(@RequestBody Hero requestHero) {
        return heroService.addHero(requestHero)
                          .map(hero -> new ResponseEntity<>(hero, HttpStatus.CREATED))
                          .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<String> updateHero(@PathVariable int id, @RequestBody Hero hero) {
        return heroService.updateHero(id, hero) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>("Unable to update hero with id: " + id, HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteHero(@PathVariable int id) {
        return heroService.deleteHero(id) ?
                new ResponseEntity<>(HttpStatus.OK) :
                new ResponseEntity<>("Unable to delete hero with id: " + id, HttpStatus.BAD_REQUEST);
    }
}
