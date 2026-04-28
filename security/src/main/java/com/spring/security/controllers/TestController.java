package com.spring.security.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
public class TestController {

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        log.info("Inside test API");
        return ResponseEntity.ok("Test passed");
    }

    @GetMapping("/")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("Logged In");
    }

}
