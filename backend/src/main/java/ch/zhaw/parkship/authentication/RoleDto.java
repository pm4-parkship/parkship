package ch.zhaw.parkship.authentication;

import java.io.Serializable;

/**
 * A DTO for the {@link Role} entity
 */
public record RoleDto(int id, String name) implements Serializable {


    public RoleDto(Role in) {
        this(in.getId(), in.getName());
    }

}