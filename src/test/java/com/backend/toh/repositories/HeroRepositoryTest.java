package com.backend.toh.repositories;

import com.backend.toh.entities.HeroDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class HeroRepositoryTest {

    @Autowired
    private HeroRepository heroRepository;

    @Test
    public void twoPersistedHeroes_findAll_returnAllHeroes() {
        List<HeroDto> heroes = new ArrayList<>();
        heroRepository.findAll().forEach(heroes::add);

        assertThat(heroes.size()).isEqualTo(2);
        assertThat(heroes.stream().map(HeroDto::getName).collect(Collectors.toList())).contains("Hero#2", "Hero#1");
    }

    @Test
    public void heroId_findById_returnHeroById() {
        Optional<HeroDto> hero = heroRepository.findById(1);

        assertThat(hero).isPresent();
        assertThat(hero.get().getName()).isEqualTo("Hero#1");
    }

    @Test
    public void heroId_delete_heroRemoved() {
        Optional<HeroDto> hero = heroRepository.findById(1);

        assertThat(hero).isPresent();
        heroRepository.delete(hero.get());

        hero = heroRepository.findById(1);
        assertThat(hero).isNotPresent();
    }

    @Test
    public void hero_update_heroUpdated() {
        String updatedHeroName = "UpdatedHero";

        Optional<HeroDto> hero = heroRepository.findById(1);
        assertThat(hero).isPresent();

        hero.get().setName(updatedHeroName);
        heroRepository.save(hero.get());

        hero = heroRepository.findById(1);
        assertThat(hero).isPresent();
        assertThat(hero.get().getName()).isEqualTo(updatedHeroName);
    }
}
