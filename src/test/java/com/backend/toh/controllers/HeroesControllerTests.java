package com.backend.toh.controllers;

import com.backend.toh.domain.Hero;
import com.backend.toh.services.HeroService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(HeroesController.class)
public class HeroesControllerTests {

    private static final String HEROES_URL = "/api/heroes";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private HeroService heroService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    }

    @Test
    public void getRequest_getHeroes_returnOkWithAllHeroes() throws Exception {
        List<Hero> heroes = Arrays.asList(buildHero(1, "Mock Hero #1"),buildHero(2, "Mock Hero #2"));

        when(heroService.getAllHeroes()).thenReturn(heroes);

        this.mockMvc.perform(get(HEROES_URL))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.length()").value(2))
                    .andExpect(jsonPath("$[0].id").value(1)).andExpect(jsonPath("$[0].name").value("Mock Hero #1"))
                    .andExpect(jsonPath("$[1].id").value(2)).andExpect(jsonPath("$[1].name").value("Mock Hero #2"));
    }

    @Test
    public void id_getHero_returnOkWithHero() throws Exception {
        int id = 1;

        when(heroService.getHeroById(id)).thenReturn(Optional.of(buildHero(id, "Mock Hero #1")));

        this.mockMvc.perform(get(HEROES_URL + "/" + id))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Mock Hero #1"));
    }

    @Test
    public void idOfNonExistentHero_getHero_returnNotFound() throws Exception {
        int id = 1;
        this.mockMvc.perform(get(HEROES_URL + "/" + id))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void hero_addHero_returnOkWithHeroAdded() throws Exception {
        Hero hero = buildHero(1, "Mock Hero #1");

        when(heroService.addHero(hero)).thenReturn(Optional.of(hero));

        this.mockMvc.perform(post(HEROES_URL).contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(hero))
                                             .with(csrf()))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").value(1)).andExpect(jsonPath("$.name").value("Mock Hero #1"));
    }

    @Test
    public void unableToAddHero_addHero_returnBadRequest() throws Exception {
        this.mockMvc.perform(post(HEROES_URL).with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void idAndHero_updateHero_returnOk() throws Exception {
        int id = 1;
        Hero hero  = buildHero(id, "Updated Hero");

        when(heroService.updateHero(id, hero)).thenReturn(true);

        this.mockMvc.perform(put(HEROES_URL + "/" + id).contentType(MediaType.APPLICATION_JSON)
                                                       .content(objectMapper.writeValueAsString(hero))
                                                       .with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void unableToUpdate_updateHero_returnBadRequest() throws Exception {
        int id = 1;
        Hero hero  = buildHero(id, "Updated Hero");

        this.mockMvc.perform(put(HEROES_URL + "/" + id).contentType(MediaType.APPLICATION_JSON)
                                                       .content(objectMapper.writeValueAsString(hero))
                                                       .with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("Unable to update hero with id: 1"));
    }

    @Test
    public void heroId_deleteHero_returnOk() throws Exception {
        int id = 1;

        when(heroService.deleteHero(id)).thenReturn(true);

        this.mockMvc.perform(delete(HEROES_URL + "/" + id).with(csrf()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void unableToDelete_deleteHero_returnBadRequest() throws Exception {
        int id = 1;
        this.mockMvc.perform(delete(HEROES_URL + "/" + id).with(csrf()))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$").value("Unable to delete hero with id: 1"));
    }

    @Test
    public void postRequestWithoutCsrf_addHero_returnForbidden() throws Exception {
        Hero hero = buildHero(1, "Mock Hero #1");

        this.mockMvc.perform(post(HEROES_URL).contentType(MediaType.APPLICATION_JSON)
                                             .content(objectMapper.writeValueAsString(hero)))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    @Test
    public void putRequestWithInvalidCsrfToken_updateHero_returnForbidden() throws Exception {
        int id = 1;
        Hero hero  = buildHero(id, "Updated Hero");

        this.mockMvc.perform(put(HEROES_URL + "/" + id).contentType(MediaType.APPLICATION_JSON)
                                                       .content(objectMapper.writeValueAsString(hero))
                                                       .with(csrf().useInvalidToken()))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$").doesNotExist());
    }

    private Hero buildHero(int id, String name) {
        return Hero.builder().id(id).name(name).build();
    }
}
