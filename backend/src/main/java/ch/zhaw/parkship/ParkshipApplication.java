package ch.zhaw.parkship;

import ch.zhaw.parkship.todos.Todo;
import ch.zhaw.parkship.todos.TodoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ParkshipApplication {

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(ParkshipApplication.class);

        if (args.length == 0) {
            springApp.setAdditionalProfiles("dev");
        }

        springApp.run(args);
    }

    @Bean
    @Profile({"dev", "production"})
    CommandLineRunner initTemplate(@Autowired TodoRepository todoRepository) {
        return args -> {
            if (todoRepository.count() == 0) {
                List<Todo> todos = Arrays.asList(
                        new Todo("Learn spring boot", "A must!"),
                        new Todo("Learn react.js", "next.js rocks!"),
                        new Todo("Learn docker", "containerize this!"),
                        new Todo("Learn typescript", "Also google monad"),
                        new Todo("Switch to Kotlin for backend", "or stay stable"),
                        new Todo("Cuddle your cats", "They deserve it")
                );
                todoRepository.saveAll(todos);
            }
        };
    }


}
