package com.backend.toh;

import com.backend.toh.controllers.HeroesController;
import com.backend.toh.controllers.UIController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class TohApplicationTests {

	@Autowired
	private HeroesController heroesController;

	@Autowired
	private UIController uiController;

	@Test
	void contextLoads() {
		assertThat(heroesController).isNotNull();
		assertThat(uiController).isNotNull();
	}

}
