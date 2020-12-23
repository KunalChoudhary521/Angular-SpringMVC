package com.backend.toh.services;

import com.backend.toh.domain.Hero;
import com.backend.toh.entities.HeroDto;
import com.backend.toh.repositories.HeroRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class HeroServiceTest {

    @Mock
    private HeroRepository heroRepository;

    @InjectMocks
    private HeroService heroService;

    @Test
    public void persistedHeroes_getAllHeroes_returnAllHeroes() {
        Iterable<HeroDto> mockHeroDtos = Arrays.asList(buildHeroDto(1, "Mock Hero#1"),buildHeroDto(2, "Mock Hero#2"));

        when(heroRepository.findAll()).thenReturn(mockHeroDtos);

        List<Hero> heroes = new ArrayList<>(heroService.getAllHeroes());

        assertThat(heroes).hasSize(2);
        assertThat(heroes.stream()
                         .map(Hero::getName)
                         .collect(Collectors.toList())).contains("Mock Hero#1", "Mock Hero#2");
        assertThat(heroes.stream()
                .map(Hero::getId)
                .collect(Collectors.toList())).contains(1, 2);
    }

    @Test
    public void heroId_getHeroById_returnHeroById() {
        int id = 1;

        Optional<HeroDto> mockHeroDto = Optional.of(buildHeroDto(1, "Mock Hero#1"));
        when(heroRepository.findById(id)).thenReturn(mockHeroDto);

        Optional<Hero> hero = heroService.getHeroById(id);

        assertThat(hero).isPresent();
        assertThat(hero.get().getName()).isEqualTo("Mock Hero#1");
    }

    @Test
    public void hero_addHero_returnHeroAdded() {
        String newHero = "New Hero";
        int id = 10;
        Hero heroToAdd = buildHero(id, newHero);

        HeroDto persistedHero = HeroDto.builder().name(newHero).build();
        when(heroRepository.save(persistedHero)).thenReturn(persistedHero);

        Optional<Hero> heroAdded = heroService.addHero(heroToAdd);

        assertThat(heroAdded).isPresent();
        assertThat(heroAdded.get().getId()).isEqualTo(0);
        assertThat(heroAdded.get().getName()).isEqualTo(newHero);
    }

    @Test
    public void heroAndId_updateHero_heroIsUpdated() {
        int id = 10;
        String name = "UpdatedHero";
        Hero hero = buildHero(id, name);

        Optional<HeroDto> mockHeroDto = Optional.of(buildHeroDto(id, "Mock Hero#1"));
        when(heroRepository.findById(id)).thenReturn(mockHeroDto);

        assertThat(heroService.updateHero(id, hero)).isTrue();
        HeroDto updatedHeroDto = buildHeroDto(id, name);
        verify(heroRepository).save(eq(updatedHeroDto));
    }

    @Test
    public void heroIdAndRequestIdsAreDifferent_updateHero_heroNotUpdated() {
        Hero hero = buildHero(10, "Updated Hero");
        assertThat(heroService.updateHero(11, hero)).isFalse();
    }

    @Test
    public void heroNotFound_updateHero_heroNotUpdated() {
        int id = 10;
        Hero hero = buildHero(id, "Updated Hero");

        when(heroRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(heroService.updateHero(id, hero)).isFalse();
        verify(heroRepository, never()).save(any(HeroDto.class));
    }

    @Test
    public void heroId_deleteHero_heroIsDeleted() {
        int id = 10;

        Optional<HeroDto> mockHeroDto = Optional.of(buildHeroDto(id, "Mock Hero#1"));
        when(heroRepository.findById(id)).thenReturn(mockHeroDto);

        assertThat(heroService.deleteHero(id)).isTrue();
        verify(heroRepository).delete(eq(mockHeroDto.get()));
    }

    @Test
    public void heroNotFound_deleteHero_heroNotDeleted() {
        int id = 10;

        when(heroRepository.findById(id)).thenReturn(Optional.empty());

        assertThat(heroService.deleteHero(id)).isFalse();
        verify(heroRepository, never()).delete(any(HeroDto.class));
    }

    private Hero buildHero(int id, String name) {
        return Hero.builder().id(id).name(name).build();
    }

    private HeroDto buildHeroDto(int id, String name) {
        return HeroDto.builder().id(id).name(name).build();
    }
}
