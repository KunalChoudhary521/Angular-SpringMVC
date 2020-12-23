package com.backend.toh.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Hero implements Comparable<Hero> {
    private int id;
    private String name;

    @Override
    public int compareTo(Hero anotherHero) {
        return this.id - anotherHero.id;
    }
}
