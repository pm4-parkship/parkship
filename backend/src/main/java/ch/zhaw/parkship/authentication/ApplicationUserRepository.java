package ch.zhaw.parkship.authentication;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Reporsitory for managing user data from the database.
 */
@Repository
public interface ApplicationUserRepository extends JpaRepository<ApplicationUser, Integer> {
    ApplicationUser findByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);
}
