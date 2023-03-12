package ch.entewurzel.parkship.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class DadJokesController {

    @GetMapping("/api/dadjokes")
    fun dadJokes(): String {
        return "What do you call an elephant that doesn’t matter? -> Irrelephant"
    }


}