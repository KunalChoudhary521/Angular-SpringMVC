package com.backend.toh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ui")
public class UIController {

    @RequestMapping(method = RequestMethod.GET)
    public String forwardToAngularApplication() {
        return "home";
    }
}
