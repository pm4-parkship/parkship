package ch.zhaw.parkship.role;

import ch.zhaw.parkship.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class for the role entity in the database.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    /**
     * User with this role.
     */
    @ManyToMany(mappedBy = "roleEntities")
    @JsonIgnore
    private Set<UserEntity> users = new HashSet<>();

    public RoleEntity(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleEntity roleEntity = (RoleEntity) o;
        return id == roleEntity.id && name.equals(roleEntity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
