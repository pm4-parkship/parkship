package ch.zhaw.parkship.user;

import ch.zhaw.parkship.user.exceptions.UserNotFoundException;
import ch.zhaw.parkship.user.exceptions.UserStateCanNotBeChanged;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Manages the user data for request over the controller for signing up and signing in.
 */
@Service
@Getter
@Setter
public class UserService implements UserDetailsService {
    /**
     * The default role is USER as this role has the lowest access level.
     */
    private static final String DEFAULT_ROLE = "USER";
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save a ApplicationUser in the database.
     *
     * @param userEntity
     * @return Saved applicationUser
     */
    public UserEntity save(UserEntity userEntity) {
        userRepository.save(userEntity);
        return userEntity;
    }

    /**
     * Get a user from the database by username.
     *
     * @param username
     * @return The user where username is the email address.
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.getParkshipUserDetailsByEmail(username);
    }

    /**
     * Signs up a new user and saves it in the database. The password is encoded and the user gets the
     * default role.
     *
     * @param email    of the new user
     * @param password of the new user
     * @return The saved new user.
     */

    public UserEntity signUp(String name, String surname, String email, String password, UserRole userRole) {
        UserEntity newUser = new UserEntity();
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setEmail(email);
        newUser.setUsername(email);
        newUser.setPassword(passwordEncoder.encode(password));
        newUser.setUserRole(userRole);
        newUser.setUserState(UserState.LOCKED);
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Checks if a users email or username already exists in the database.
     *
     * @param email
     * @return true, if the email or username already exists in the database.
     */

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void changeUserState(Long userId, UserState userState) throws UserNotFoundException, UserStateCanNotBeChanged {
        Optional<UserEntity> userOptional = userRepository.findById(userId);
        if(userOptional.isEmpty()) {
            throw new UserNotFoundException("User with id " + userId + " not found");
        }

        if(userOptional.get().getUserState().equals(userState)) {
            throw new UserStateCanNotBeChanged("User already has state " + userState);
        }
        UserEntity user = userOptional.get();
        user.setUserState(userState);
        save(user);
    }

    /**
     * Retrieves a list of all users in the database.
     *
     * @return a List of UserDto objects representing all users in the database
     */
    public List<UserDto> getAll() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }


}
