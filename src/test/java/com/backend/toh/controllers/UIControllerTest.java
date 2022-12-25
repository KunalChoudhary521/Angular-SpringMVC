package com.backend.toh.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@ExtendWith(SpringExtension.class)
@WebMvcTest(UIController.class)
@WithMockUser
public class UIControllerTest {

    private static final String UI_URL = "/ui";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void getRequest_displayJsp_returnHomeView() throws Exception {
        this.mockMvc.perform(get(UI_URL + "/jsp"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/WEB-INF/jsp/home.jsp"))
                    .andExpect(view().name("home"));
    }

    @Test
    public void getRequest_forwardToAngular_returnAngularIndexHtml() throws Exception {
        this.mockMvc.perform(get(UI_URL))
                    .andExpect(status().isOk())
                    .andExpect(forwardedUrl("/ui/index.html"));
    }
}
