package com.example.demo.view;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @Value("${spring.config.activate.on-profile}")
    private String activeProfile;

    @GetMapping("/")
    public String index() {
        System.out.println(activeProfile + "_PROFILE");
        return "index";
    }
}
