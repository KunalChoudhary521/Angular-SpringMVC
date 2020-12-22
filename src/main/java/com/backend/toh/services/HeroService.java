package com.backend.toh.services;

import com.backend.toh.domain.Hero;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;

public class HeroService {

    private static final Set<Hero> heroes = initialHeroes();
    private static int currentId = findMaxHeroId();

    public Collection<Hero> getAllHeroes() {
        return heroes;
    }

    public Optional<Hero> getHeroById(int id) {
        return heroes.stream().filter(hero -> hero.getId() == id).findFirst();
    }

    public Optional<Hero> addHero(Hero hero) {
        Hero newHero = new Hero(hero);
        currentId++;
        newHero.setId(currentId);

        return heroes.add(newHero) ? Optional.of(newHero) : Optional.empty();
    }

    public boolean updateHero(int id, Hero hero) {
        if (id == hero.getId()) {
            Optional<Hero> optionalHero = getHeroById(id);
            if (optionalHero.isPresent()) {
                optionalHero.get().setName(hero.getName());
                return true;
            }
        }
        return false;
    }

    public boolean deleteHero(int id) {
        Optional<Hero> optionalHero = getHeroById(id);
        return optionalHero.isPresent() && heroes.remove(optionalHero.get());
    }

    private static int findMaxHeroId() {
        return heroes.stream()
                .map(Hero::getId).max(Comparator.comparingInt(a -> a))
                .orElseThrow(() -> new RuntimeException("ERROR: CurrentId not found!"));
    }

    private static Set<Hero> initialHeroes() {
        return new TreeSet<>(Arrays.asList(
                new Hero(20, "Tornado"),
                new Hero(11, "Dr Nice"),
                new Hero(12, "Narco"),
                new Hero(13, "Bombasto"),
                new Hero(14, "Celeritas"),
                new Hero(15, "Magneta"),
                new Hero(16, "RubberMan"),
                new Hero(17, "Dynama"),
                new Hero(18, "Dr IQ"),
                new Hero(19, "Magma")));
    }
}
