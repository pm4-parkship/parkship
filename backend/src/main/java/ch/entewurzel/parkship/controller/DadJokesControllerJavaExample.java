package ch.entewurzel.parkship.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DadJokesControllerJavaExample {
    @GetMapping("/api/dadjokesjava")
    public String dadJokes() {
        return "Java What do you call an elephant that doesn’t matter? -> Irrelephant";
    }
}
