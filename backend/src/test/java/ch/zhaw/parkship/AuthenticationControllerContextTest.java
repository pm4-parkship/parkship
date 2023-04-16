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
import org.springframework.web.server.ResponseStatusException;

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
    void testSignUp() {
        //New user
        AuthenticationController.SignUpRequestDTO signUpRequestDTO = new AuthenticationController.SignUpRequestDTO("test@test.ch", "tester", "testest");
        AuthenticationController.SignUpResponseDTO signUpResponseDTO = authenticationController.signUp(signUpRequestDTO);

        assertNotNull(signUpResponseDTO, "SignUpResponseDTO is null");
        assertNotNull(signUpResponseDTO.id(), "User id in response is null");
        assertEquals("tester", signUpResponseDTO.username(), "Username in response is not correct");
        assertThat(signUpResponseDTO.roles()).contains("USER");

        //Email already exists
        AuthenticationController.SignUpRequestDTO signUpRequestDTOEmailExists = new AuthenticationController.SignUpRequestDTO("user@parkship.ch", "tester", "testest");

        assertThrows(ResponseStatusException.class, () -> {
            authenticationController.signUp(signUpRequestDTOEmailExists);
        }, "No exception thrown, because email already exists");

        //Username already exists
        AuthenticationController.SignUpRequestDTO signUpRequestDTOUsernameExists = new AuthenticationController.SignUpRequestDTO("neu@neu.ch", "user", "testest");

        assertThrows(ResponseStatusException.class, () -> {
            authenticationController.signUp(signUpRequestDTOUsernameExists);
        }, "No exception thrown, because username already exists");
    }

    @Test
    void testSignIn() {
        assertTrue(userService.existsByEmailOrUsername("user@parkship.ch", "user"), "Dummy user for test does not exist");

        //Existing user right password
        AuthenticationController.SignInRequestDTO signInRequestDTO = new AuthenticationController.SignInRequestDTO("user@parkship.ch", "user");
        AuthenticationController.SignInResponseDTO signInResponseDTO = authenticationController.signIn(signInRequestDTO);

        assertNotNull(signInResponseDTO.token(), "Response contains no token");
        assertEquals("user", signInResponseDTO.username(), "Response contains wrong username");
        assertThat(signInResponseDTO.roles()).contains("USER");

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
