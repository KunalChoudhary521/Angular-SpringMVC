package com.backend.toh.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Hero implements Comparable<Hero> {
    private int id;
    private String name;

    public Hero(Hero anotherHero) {
        this.id = anotherHero.id;
        this.name = anotherHero.name;
    }

    @Override
    public int compareTo(Hero anotherHero) {
        return this.id - anotherHero.id;
    }
}
