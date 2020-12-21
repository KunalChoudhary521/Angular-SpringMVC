package com.backend.toh.controllers;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping(value = "/api/heroes")
public class HeroesController {

    @RequestMapping(method = RequestMethod.GET)
    public List<String> getHeroes() {
        return Arrays.asList("Hero#1", "Hero#2", "Hero#3");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public String getHero(@PathVariable String id) {
        return "Hero: " + id;
    }

}
