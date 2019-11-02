package com.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class Main {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Main.class);
        springApplication.run(args);
    }

    @GetMapping("/")
    public ResponseEntity<Void> index2(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/index")
    public ResponseEntity<Void> index(){
        System.out.println(232);
        return ResponseEntity.ok().build();
    }
}
