package ch.zhaw.parkship.role;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository for manging role data from the database.
 */
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String name);
}
