package com.backend.toh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui")
public class UIController {

    @GetMapping(value = "/jsp")
    public String displayJspFile() {
        return "home";
    }

    @GetMapping()
    public String forwardToAngular() {
        return "forward:/ui/index.html";
    }
}
