package ch.zhaw.parkship.user;

import java.io.Serializable;
import java.util.Objects;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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
  @Email
  private String email;

  public UserDto(UserEntity userEntity) {
    this.id = userEntity.getId();
    this.name = userEntity.getName();
    this.surname = userEntity.getSurname();
    this.email = userEntity.getApplicationUser().getEmail();
  }

  public UserDto() {}

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    return id.equals(((UserDto) o).getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "UserDto{" + "id=" + id + ", name='" + name + '\'' + ", surname='" + surname + '\''
        + ", email='" + email + '\'' + '}';
  }

}
