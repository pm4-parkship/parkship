package ch.zhaw.parkship;

import ch.zhaw.parkship.authentication.*;
import ch.zhaw.parkship.examples.todos.Todo;
import ch.zhaw.parkship.examples.todos.TodoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class ParkshipApplication {

    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(ParkshipApplication.class);

        if (args.length == 1) {
            springApp.setAdditionalProfiles("dev");
        }

        springApp.run(args);
    }

    @Bean
    @Profile({"dev", "test"})
    @Transactional
    CommandLineRunner initTemplate(@Autowired TodoRepository todoRepository,
                                   @Autowired RoleRepository roleRepository, UserService userService) {
        return args -> {
            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");

            roleRepository.saveAll(Set.of(userRole, adminRole));

            ApplicationUser user = userService.signUp("user", "user@parkship.ch", "user");
            ApplicationUser secondUser = userService.signUp("second", "second@parkship.ch", "second");
            ApplicationUser thirdUser = userService.signUp("thirdUser", "thirdUser@parkship.ch", "thirdUser");

            ApplicationUser admin = userService.signUp("admin", "admin@parkship.ch", "admin");

            user.getRoles().add(userRole);
            secondUser.getRoles().add(userRole);
            thirdUser.getRoles().add(userRole);
            admin.getRoles().add(adminRole);
            userService.save(user);
            userService.save(secondUser);
            userService.save(thirdUser);
            userService.save(admin);

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
