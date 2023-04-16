package ch.zhaw.parkship;

import ch.zhaw.parkship.ParkshipApplication;
import org.springframework.boot.SpringApplication;
/**
 * This runnable program starts up the complete application
 * with a h2 db locally using an embedded web-container.
 */
public class StandaloneApplication {

    /**
     * Starts up the application
     */
    public static void main(String[] args) {
        SpringApplication springApp = new SpringApplication(ParkshipApplication.class);
        springApp.setAdditionalProfiles("dev");
        springApp.run(args);
    }


}
