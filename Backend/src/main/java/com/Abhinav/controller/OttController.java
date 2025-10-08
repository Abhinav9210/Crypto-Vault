package com.Abhinav.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@RestController
@RequestMapping("/")
public class OttController {
    @GetMapping("/")
    public Map<String, String> sentt() {
        return Map.of("ullu"," your email.");
    }

    @GetMapping("/ott/sent")
    public Map<String, String> sent() {
        return Map.of("message", "Magic link has been sent. Check your email.");
    }
    @PostMapping("/login/ott")
    public Map<String, String> sen() {
        return Map.of("message", "Login///// link has been sent. Check your email.");
    }

}
