package ch.zhaw.parkship.user;


import ch.zhaw.parkship.user.exceptions.UserNotFoundException;
import ch.zhaw.parkship.user.exceptions.UserStateCanNotBeChanged;
import ch.zhaw.parkship.util.ParkshipUserDetailsContext;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final PasswordGeneratorService passwordGeneratorService;


    /**
     * Record for having the users request from the frontend to sign up in one clean object.
     *
     */
    public record SignUpRequestDTO(@NotBlank String name, @NotBlank String surname, @NotBlank String username, @NotNull UserRole role) {
    }

    /**
     * Record for having the response to a users sign up request in one clean object.
     *
     * @param id
     * @param username
     */
    public record SignUpResponseDTO(Long id, String name, String surname, String username, UserRole userRole, String password) {
    }


    /**
     * Retrieves a list of all users. Only consumable by users with ADMIN role.
     *
     * @return ResponseEntity with a list of UserDto objects in the body, or a no content response if the list is empty
     */
    @GetMapping(produces = "application/json")
    @Secured("ADMIN")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> userDtos = userService.getAll();
        if (userDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(userDtos);
    }


    /**
     * Processes a users request to sign up. If the user already exists, it throws an error and if
     * not, it registers the user and sends a response to the frontend, with a generated password.
     *
     * @param signUpRequestDTO Sign up request from the frontend of a user.
     * @return Response to the frontend if all user information.
     */
    @PostMapping("/signup")
    @Secured("ADMIN")
    public SignUpResponseDTO signUp(@Valid @RequestBody SignUpRequestDTO signUpRequestDTO) {
        if (userService.existsByUsername(signUpRequestDTO.username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        String password = passwordGeneratorService.generatePassword();
        UserEntity newUser = userService.signUp(signUpRequestDTO.name, signUpRequestDTO.surname, signUpRequestDTO.username,
                password, signUpRequestDTO.role);
        return new SignUpResponseDTO(newUser.getId(),
                newUser.getName(),
                newUser.getSurname(),
                newUser.getUsername(),
                newUser.getUserRole(),
                password);
    }

    @Secured("ADMIN")
    @PutMapping("/{id}/lock")
    public ResponseEntity lockUser(@PathVariable("id") Long id) throws UserNotFoundException, UserStateCanNotBeChanged {
        userService.changeUserState(id, UserState.LOCKED);
        return ResponseEntity.ok().build();
    }

    @Secured("ADMIN")
    @PutMapping("/{id}/unlock")
    public ResponseEntity unlockUser(@PathVariable("id") Long id) throws UserNotFoundException, UserStateCanNotBeChanged {
        userService.changeUserState(id, UserState.UNLOCKED);
        return ResponseEntity.ok().build();

    }

    @GetMapping(value = "/user", produces = "application/json")
    public ResponseEntity<UserDto> getUser(@AuthenticationPrincipal ParkshipUserDetails userDetails) {
        ParkshipUserDetails user = ParkshipUserDetailsContext.getCurrentParkshipUserDetails();
        return ResponseEntity.ok(new UserDto(user.getId(), user.getUsername(), user.getName(), user.getSurname(), user.getUserRole(), user.getUserState()));
    }

    @GetMapping(value = "/roles", produces = "application/json")
    public ResponseEntity<List<UserRole>> getUserRoles() {
        return ResponseEntity.ok(Arrays.asList(UserRole.values()));
    }
}