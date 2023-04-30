package ch.zhaw.parkship.user;

import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.role.RoleRepository;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.role.RoleRepository;
import lombok.Getter;
import lombok.Setter;

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
  private RoleRepository roleRepository;
  private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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

    public UserEntity signUp(String username, String email, String password) {
        UserEntity newUser = new UserEntity();
        newUser.setEmail(email);
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password));
        RoleEntity userRoleEntity = roleRepository.findByName(DEFAULT_ROLE);
        newUser.getRoleEntities().add(userRoleEntity);
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
