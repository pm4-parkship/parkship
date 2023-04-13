package ch.zhaw.parkship.authentication;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A DTO for the {@link ApplicationUser} entity
 */
public record ApplicationUserDto(Long id, @NotBlank @Email String email, @NotBlank String username, String name,
                                 String surname, Set<RoleDto> roles) implements Serializable {


    public ApplicationUserDto(ApplicationUser in) {
        this(in.getId(), in.getEmail(), in.getUsername(), in.getName(), in.getSurname(),
                in.getRoles().stream().map(RoleDto::new).collect(Collectors.toSet()));
    }
}