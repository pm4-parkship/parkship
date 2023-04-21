package ch.zhaw.parkship.role;

import java.io.Serializable;

/**
 * A DTO for the {@link RoleEntity} entity
 */
public record RoleDto(int id, String name) implements Serializable {


    public RoleDto(RoleEntity in) {
        this(in.getId(), in.getName());
    }

}