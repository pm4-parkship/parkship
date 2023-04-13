package ch.zhaw.parkship;

import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.role.RoleRepository;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.Set;

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
    @Profile({"dev", "test"})
    @Transactional
    CommandLineRunner initTemplate(@Autowired RoleRepository roleRepository,
                                   UserService userService) {
        return args -> {
            RoleEntity userRoleEntity = new RoleEntity("USER");
            RoleEntity adminRoleEntity = new RoleEntity("ADMIN");

            roleRepository.saveAll(Set.of(userRoleEntity, adminRoleEntity));

            UserEntity user = userService.signUp("user", "user@parkship.ch", "user");
            UserEntity secondUser = userService.signUp("second", "second@parkship.ch", "second");
            UserEntity thirdUser = userService.signUp("thirdUser", "thirdUser@parkship.ch", "thirdUser");
            UserEntity admin = userService.signUp("admin", "admin@parkship.ch", "admin");


            user.getRoleEntities().add(userRoleEntity);
            secondUser.getRoleEntities().add(userRoleEntity);
            thirdUser.getRoleEntities().add(userRoleEntity);
            admin.getRoleEntities().add(adminRoleEntity);

            userService.save(user);
            userService.save(secondUser);
            userService.save(thirdUser);
            userService.save(admin);
        };
    }

}
