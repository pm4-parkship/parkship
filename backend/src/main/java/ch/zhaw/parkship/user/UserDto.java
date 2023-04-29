package ch.zhaw.parkship.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

/**
 * A DTO for the {@link UserEntity} entity
 */
public record UserDto(Long id, @NotBlank @Email String email, @NotBlank String username, String name,
                      String surname, UserRole role, UserState userState) implements Serializable {


    public UserDto(UserEntity in) {
        this(in.getId(), in.getEmail(), in.getUsername(), in.getName(), in.getSurname(), in.getUserRole(), in.getUserState());
    }
}