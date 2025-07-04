package net.javaguides.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String welcomePage() {
        return "welcome"; // returns welcome.html from templates
    }
}
