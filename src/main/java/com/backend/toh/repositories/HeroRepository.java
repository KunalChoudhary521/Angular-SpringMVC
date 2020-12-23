package com.backend.toh.repositories;

import com.backend.toh.entities.HeroDto;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeroRepository extends CrudRepository<HeroDto, Integer> {
}
