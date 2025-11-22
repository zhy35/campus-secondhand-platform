package com.campus.secondhand.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/simple")
public class SimpleController {

    @GetMapping("/test")
    public String test() {
        return "Simple Controller Test OK!";
    }

    @GetMapping("/hello")
    public String hello() {
        return "Hello World!";
    }
}