package ch.zhaw.parkship;

import ch.zhaw.parkship.user.*;
import ch.zhaw.parkship.user.exceptions.UserNotFoundException;
import ch.zhaw.parkship.user.exceptions.UserStateCanNotBeChanged;
import ch.zhaw.parkship.util.AbstractDataRollbackTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the UserService class in context of the database.
 */
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class UserServiceContextTest extends AbstractDataRollbackTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void init() {
        doSeed();
    }

    @Test
    public void testSignUp() {
        UserEntity userEntity = userService.signUp("test2", "test2Surname",  "test2@test.ch", "test2",  UserRole.USER);
        assertTrue(userService.existsByUsername("test2@test.ch"), "Registered email does not exist");
        assertEquals("test2@test.ch", userEntity.getUsername(), "Email is not correct");
        assertNotEquals("testtest2", userEntity.getPassword(), "Password is not encoded.");
        UserEntity userEntity1 = userRepository.findByUsername("test2@test.ch");
        assertEquals(UserState.LOCKED, userEntity1.getUserState(), "Wrong default userstate");

    }

    @Test
    public void testChangeUserState() {
        userService.signUp("test2", "test2Surname",  "test2@test.ch", "test2",  UserRole.USER);
        UserEntity userEntity1 = userRepository.findByUsername("test2@test.ch");
        Long userID = userEntity1.getId();

        try {
            userService.changeUserState(userID, UserState.UNLOCKED);
        } catch (Exception e) {
            fail(e);
        }
        userEntity1 = userRepository.findByUsername("test2@test.ch");
        assertEquals(UserState.UNLOCKED, userEntity1.getUserState());

        assertThrows(UserStateCanNotBeChanged.class, () -> {
            userService.changeUserState(userID, UserState.UNLOCKED);

        }, "No exception thrown even if user already has wanted state");

        assertThrows(UserNotFoundException.class, () -> {
            userService.changeUserState(123819238L, UserState.UNLOCKED);

        }, "No exception thrown for non-existent user");



    }



}
