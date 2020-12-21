package com.backend.toh.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/ui")
public class UIController {

    @RequestMapping(value = "/jsp", method = RequestMethod.GET)
    public String displayJspFile() {
        return "home";
    }

    @RequestMapping(value = "/ng", method = RequestMethod.GET)
    public String forwardToAngular() {
        return "forward:/ui/index.html"; // alternatively: "forward:/ng-dist/index.html";
    }
}
