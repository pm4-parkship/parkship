package ch.zhaw.parkship.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Reporsitory for managing user data from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUsername(String email);

    @Query("select new ch.zhaw.parkship.user.ParkshipUserDetails(" +
            "u.id, u.username, u.name, u.surname, u.password, u.userRole, u.userState) "+
            " from UserEntity u where u.username = ?1")
    ParkshipUserDetails getParkshipUserDetailsByUsername(String username);


    @Query("select new ch.zhaw.parkship.user.ParkshipUserDetails(" +
            "u.id, u.username, u.name, u.surname, u.password, u.userRole, u.userState) "+
            " from UserEntity u where u.id = ?1")
    ParkshipUserDetails getParkshipUserDetailsById(Long id);

    boolean existsByUsername(String username);
}
