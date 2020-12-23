package com.backend.toh.mappers;

import com.backend.toh.domain.Hero;
import com.backend.toh.entities.HeroDto;


public class HeroMapper {

    public static Hero heroDtoToHero(HeroDto heroDto) {
        return Hero.builder().id(heroDto.getId()).name(heroDto.getName()).build();
    }

    public static HeroDto heroToHeroDto(Hero hero) {
        return HeroDto.builder().name(hero.getName()).build();
    }
}
