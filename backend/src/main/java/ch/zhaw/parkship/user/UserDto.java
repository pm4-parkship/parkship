package ch.zhaw.parkship.user;

import ch.zhaw.parkship.role.RoleDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link UserEntity} entity
 */
public record UserDto(Long id, @NotBlank @Email String email, @NotBlank String username, String name,
                      String surname, Set<RoleDto> roles) implements Serializable {


    public UserDto(UserEntity in) {
        this(in.getId(), in.getEmail(), in.getUsername(), in.getName(), in.getSurname(),
                in.getRoleEntities().stream().map(RoleDto::new).collect(Collectors.toSet()));
    }
}