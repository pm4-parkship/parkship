package ch.zhaw.parkship.authentication;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;

/**
 * Manages the user data for request over the controller for signing up and
 * signing in.
 */
@Service
@Getter
@Setter
public class ApplicationUserService implements UserDetailsService {
    /**
     * The default role is USER as this role has the lowest access level.
     */
    private static final String DEFAULT_ROLE = "USER";
    private ApplicationUserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    public ApplicationUserService(ApplicationUserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    /**
     * Save a ApplicationUser in the database.
     *
     * @param applicationUser
     * @return Saved applicationUser
     */
    public ApplicationUser save(ApplicationUser applicationUser) {
        userRepository.save(applicationUser);
        return applicationUser;
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
        return userRepository.findByEmail(username);
    }

    /**
     * Signs up a new user and saves it in the database. The password is encoded and
     * the user gets the default role.
     *
     * @param username of the new user
     * @param email    of the new user
     * @param password of the new user
     * @return The saved new user.
     */
    @Transactional
    public ApplicationUser signUp(String username, String email, String password) {
        ApplicationUser newUser = new ApplicationUser();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        Role userRole = roleRepository.findByName(DEFAULT_ROLE);
        newUser.getRoles().add(userRole);
        userRepository.save(newUser);
        return newUser;
    }

    /**
     * Checks if a users email or username already exists in the database.
     *
     * @param email
     * @param username
     * @return true, if the email or username already exists in the database.
     */
    public boolean existsByEmailOrUsername(String email, String username) {
        return userRepository.existsByEmailOrUsername(email, username);
    }

}
