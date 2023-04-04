package ch.zhaw.parkship.authentication;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manging role data from the database.
 */
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
