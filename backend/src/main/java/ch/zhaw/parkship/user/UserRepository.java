package ch.zhaw.parkship.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Reporsitory for managing user data from the database.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    @Query("select new ch.zhaw.parkship.user.ParkshipUserDetails(" +
            "u.id, u.email, u.username, u.name, u.surname, u.password, u.userRole, u.userState) "+
            " from UserEntity u where u.email = ?1")
    ParkshipUserDetails getParkshipUserDetailsByEmail(String email);


    @Query("select new ch.zhaw.parkship.user.ParkshipUserDetails(" +
            "u.id, u.email, u.username, u.name, u.surname, u.password, u.userRole, u.userState) "+
            " from UserEntity u where u.id = ?1")
    ParkshipUserDetails getParkshipUserDetailsById(Long id);
    boolean existsByEmail(String email);
}
