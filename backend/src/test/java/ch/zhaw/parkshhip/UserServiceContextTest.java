package ch.zhaw.parkshhip;


import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.authentication.ApplicationUser;
import ch.zhaw.parkship.authentication.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the UserService class in context of the database.
 */
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class UserServiceContextTest {

    @Autowired
    UserService userService;

    @Test
    public void testSignUp(){
        ApplicationUser applicationUser = userService.signUp("test2", "test2@test.ch", "testtest2");
        assertTrue(userService.existsByEmailOrUsername("tesyxcyxct@test.ch", "test2"), "Registered username does not exist");
        assertTrue(userService.existsByEmailOrUsername("test2@test.ch", "asdsad"), "Registered email does not exist");
        assertEquals( "test2@test.ch", applicationUser.getEmail(), "Email is not correct");
        assertEquals("test2", applicationUser.getUsername(), "Username is not correct");
        assertNotEquals("testtest2", applicationUser.getPassword(), "Password is not encoded.");

    }

}
