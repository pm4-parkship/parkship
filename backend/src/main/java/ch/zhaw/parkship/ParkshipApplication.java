package ch.zhaw.parkship;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import ch.zhaw.parkship.authentication.ApplicationUser;
import ch.zhaw.parkship.authentication.ApplicationUserService;
import ch.zhaw.parkship.authentication.Role;
import ch.zhaw.parkship.authentication.RoleRepository;
import ch.zhaw.parkship.examples.todos.Todo;
import ch.zhaw.parkship.examples.todos.TodoRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import jakarta.transaction.Transactional;

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

    @Profile({ "dev", "test" })
    @Transactional
    CommandLineRunner initTemplate(@Autowired TodoRepository todoRepository, @Autowired RoleRepository roleRepository,
            @Autowired UserRepository userRepository, ApplicationUserService userService,
            ParkingLotService parkingLotService, ReservationService reservationService) {
        return args -> {
            Role userRole = new Role("USER");
            Role adminRole = new Role("ADMIN");

            roleRepository.saveAll(Set.of(userRole, adminRole));

            ApplicationUser user = userService.signUp("user", "user@parkship.ch", "user");
            ApplicationUser secondUser = userService.signUp("second", "second@parkship.ch", "second");
            ApplicationUser thirdUser = userService.signUp("thirdUser", "thirdUser@parkship.ch", "thirdUser");
            ApplicationUser admin = userService.signUp("admin", "admin@parkship.ch", "admin");

            UserEntity userEntity = userRepository.save(new UserEntity("Fritz", "Fröhlich"));
            UserEntity secondUserEntity = userRepository.save(new UserEntity("Sue", "Moe"));
            UserEntity thirdUserEntity = userRepository.save(new UserEntity("Anne", "Bananne"));
            UserEntity adminEntity = userRepository.save(new UserEntity("CHuck", "Huck"));

            userEntity.setApplicationUser(user);
            secondUserEntity.setApplicationUser(secondUser);
            thirdUserEntity.setApplicationUser(thirdUser);
            adminEntity.setApplicationUser(admin);

            user.getRoles().add(userRole);
            secondUser.getRoles().add(userRole);
            thirdUser.getRoles().add(userRole);
            admin.getRoles().add(adminRole);

            userService.save(user);
            userService.save(secondUser);
            userService.save(thirdUser);
            userService.save(admin);

            userRepository.save(userEntity);
            userRepository.save(secondUserEntity);
            userRepository.save(thirdUserEntity);
            userRepository.save(adminEntity);

            if (todoRepository.count() == 0) {
                List<Todo> todos = Arrays.asList(new Todo("Learn spring boot", "A must!"),
                        new Todo("Learn react.js", "next.js rocks!"), new Todo("Learn docker", "containerize this!"),
                        new Todo("Learn typescript", "Also google monad"),
                        new Todo("Switch to Kotlin for backend", "or stay stable"),
                        new Todo("Cuddle your cats", "They deserve it"));
                todoRepository.saveAll(todos);
            }
        };
    }

}
