package ch.zhaw.parkship.authentication;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
 * Controller for signing up and signing in resp. for the registration and the
 * login of a normal user.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    /**
     * Record for having the users request from the frontend to sign in in one clean
     * object.
     *
     * @param email    Users email address.
     * @param password Users password in plain text.
     */
    public record SignInRequestDTO(@NotBlank String email, @NotBlank String password) {
    }

    /**
     * Record for having the response to a users sign in request in one clean
     * object.
     *
     * @param token    JWT token.
     * @param username
     * @param roles    Roles of the user.
     */
    public record SignInResponseDTO(String token, String username, Set<String> roles) {
    }

    /**
     * Record for having the users request from the frontend to sign up in one clean
     * object.
     *
     * @param email
     * @param username
     * @param password
     */
    public record SignUpRequestDTO(@NotBlank @Email String email, @NotBlank String username,
            @NotBlank String password) {
    }

    /**
     * Record for having the response to a users sign up request in one clean
     * object.
     *
     * @param id
     * @param username
     * @param roles
     */
    public record SignUpResponseDTO(int id, String username, Set<String> roles) {
    }

    private final ApplicationUserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /**
     * Constructor for the AuthenticationController.
     *
     * @param userService           for getting and saving user information
     * @param jwtService            for generating JWT tokens.
     * @param authenticationManager
     */
    public AuthenticationController(ApplicationUserService userService, JwtService jwtService,
            AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Processes a users request to sign up. If the user already exists, it throws
     * an error and if not, it registers the user and sends a response to the
     * frontend.
     *
     * @param signUpRequestDTO Sign up request from the frontend of a user.
     * @return Response to the frontend if all user information.
     */
    @PostMapping("/signup")
    public SignUpResponseDTO signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        if (userService.existsByEmailOrUsername(signUpRequestDTO.email, signUpRequestDTO.username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        ApplicationUser newUser = userService.signUp(signUpRequestDTO.username, signUpRequestDTO.email,
                signUpRequestDTO.password);
        return new SignUpResponseDTO(newUser.getId(), newUser.getUsername(),
                newUser.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }

    /**
     * Processes a users request to sign in. First it authenticates the user, if the
     * user is not authenticated, it throws an error and if not, it
     *
     * @param signInRequestDTO
     * @return
     */
    @PostMapping("/signin")
    public SignInResponseDTO signIn(@Valid @RequestBody SignInRequestDTO signInRequestDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(signInRequestDTO.email, signInRequestDTO.password));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        String token = jwtService.generateToken(user);
        return new SignInResponseDTO(token, user.getUsername(),
                user.getRoles().stream().map(Role::getName).collect(Collectors.toSet()));
    }
}
