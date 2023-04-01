package ch.zhaw.parkship.dtos;

import java.io.Serializable;
import java.util.Set;

import javax.management.relation.Role;

import ch.zhaw.parkship.entities.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto implements Serializable {
	private Long id;

	@NotBlank
	private String name;

	@NotBlank
	private String surname;

	@NotBlank
	private String password;

	@NotEmpty
	private Set<Role> roles;

	@NotBlank
	@Email
	private String email;

	public UserDto(UserEntity userEntity) {
		this.id = userEntity.getId();
		this.name = userEntity.getName();
		this.surname = userEntity.getSurname();
		this.password = userEntity.getPassword();
		this.roles = userEntity.getRoles();
		this.email = userEntity.getEmail();
	}

	public UserDto() {
	}
}
