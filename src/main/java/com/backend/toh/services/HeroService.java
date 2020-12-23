package com.backend.toh.services;

import com.backend.toh.domain.Hero;
import com.backend.toh.entities.HeroDto;
import com.backend.toh.mappers.HeroMapper;
import com.backend.toh.repositories.HeroRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public class HeroService {

    private final HeroRepository heroRepository;

    public HeroService(HeroRepository heroRepository) {
        this.heroRepository = heroRepository;
    }

    public Collection<Hero> getAllHeroes() {
        List<Hero> heroes = new ArrayList<>();
        heroRepository.findAll().forEach(hero -> heroes.add(HeroMapper.heroDtoToHero(hero)));
        return heroes;
    }

    public Optional<Hero> getHeroById(int id) {
        return getHeroDtoById(id).map(HeroMapper::heroDtoToHero);
    }

    public Optional<Hero> addHero(Hero hero) {
        return Optional.of(heroRepository.save(HeroMapper.heroToHeroDto(hero)))
                       .map(HeroMapper::heroDtoToHero);
    }

    public boolean updateHero(int id, Hero hero) {
        if (id == hero.getId()) {
            Optional<HeroDto> oldHeroDto = getHeroDtoById(id);
            if (oldHeroDto.isPresent()) {
                HeroDto updatedHeroDto = HeroMapper.heroToHeroDto(hero);
                updatedHeroDto.setId(oldHeroDto.get().getId());
                heroRepository.save(updatedHeroDto);
                return true;
            }
        }
        return false;
    }

    public boolean deleteHero(int id) {
        Optional<HeroDto> heroDto = getHeroDtoById(id);
        if(heroDto.isPresent()) {
            heroRepository.delete(heroDto.get());
            return true;
        }
        return false;
    }

    private Optional<HeroDto> getHeroDtoById(int id) {
        return heroRepository.findById(id);
    }
}
