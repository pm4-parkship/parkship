package ch.zhaw.parkship.authentication;

import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserRole;
import ch.zhaw.parkship.user.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for signing up and signing in resp. for the registration and the login of a normal
 * user.
 */
@RestController
@RequestMapping("/auth")
@Transactional
public class AuthenticationController {
    /**
     * Record for having the users request from the frontend to sign in in one clean object.
     *
     * @param email    Users email address.
     * @param password Users password in plain text.
     */
    public record SignInRequestDTO(@NotBlank String email, @NotBlank String password) {
    }

    /**
     * Record for having the response to a users sign in request in one clean object.
     *
     * @param token    JWT token.
     * @param username
     */
    public record SignInResponseDTO(String token, String username, String name, UserRole role) {
    }

    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    /**
     * Constructor for the AuthenticationController.
     *
     * @param userService           for getting and saving user information
     * @param jwtService            for generating JWT tokens.
     * @param authenticationManager
     */
    public AuthenticationController(UserService userService, JwtService jwtService,
                                    AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }


    /**
     * Processes a users request to sign in. First it authenticates the user, if the user is not
     * authenticated, it throws an error and if not, it
     *
     * @param signInRequestDTO
     * @return
     */
    @PostMapping("/signin")
    public SignInResponseDTO signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDTO.email, signInRequestDTO.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ParkshipUserDetails parkshipUserDetails = (ParkshipUserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(parkshipUserDetails);
        return new SignInResponseDTO(token, parkshipUserDetails.getUsername(), parkshipUserDetails.getName(), parkshipUserDetails.getUserRole());
    }
}
