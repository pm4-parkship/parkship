package ch.zhaw.parkship;

import ch.zhaw.parkship.authentication.AuthenticationController;
import ch.zhaw.parkship.util.AbstractDataRollbackTest;
import ch.zhaw.parkship.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


/**
 * Tests the  basic functionality of th AuthenticationController
 * in context of the whole application.
 */
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
class AuthenticationControllerContextTest extends AbstractDataRollbackTest {


    @Autowired
    UserService userService;

    @Autowired
    AuthenticationController authenticationController;


    @BeforeEach
    void init() {
        doSeed();
    }


    @Test
    void testSignIn() {
        assertTrue(userService.existsByEmail("user@parkship.ch"), "Dummy user for test does not exist");

        //Existing user right password
        AuthenticationController.SignInRequestDTO signInRequestDTO = new AuthenticationController.SignInRequestDTO("user@parkship.ch", "user");
        AuthenticationController.SignInResponseDTO signInResponseDTO = authenticationController.signIn(signInRequestDTO);

        assertNotNull(signInResponseDTO.token(), "Response contains no token");
        assertEquals("USER", signInResponseDTO.role().name(), "Response contains wrong role");

        //Existing user wrong password
        AuthenticationController.SignInRequestDTO signInRequestDTOWrongPW = new AuthenticationController.SignInRequestDTO("user@parkship.ch", "blubasd");

        assertThrows(BadCredentialsException.class, () -> {
            authenticationController.signIn(signInRequestDTOWrongPW);

        }, "No exception thrown for wrong password");

        //Non-existent user
        AuthenticationController.SignInRequestDTO signInRequestDTOWrongUser = new AuthenticationController.SignInRequestDTO("useasddsasar@parkship.ch", "blubasd");
        assertThrows(InternalAuthenticationServiceException.class, () -> {
            authenticationController.signIn(signInRequestDTOWrongUser);

        }, "No exception thrown for non-existent user");

    }


}
