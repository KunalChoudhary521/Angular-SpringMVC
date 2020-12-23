package com.backend.toh.config;

import com.backend.toh.repositories.HeroRepository;
import com.backend.toh.services.HeroService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public HeroService heroService(HeroRepository heroRepository) {
        return new HeroService(heroRepository);
    }
}
