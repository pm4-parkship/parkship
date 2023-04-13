package ch.zhaw.parkship.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Reporsitory for managing user data from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    boolean existsByEmailOrUsername(String email, String username);
}
