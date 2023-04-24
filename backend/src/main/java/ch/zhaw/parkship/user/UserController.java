package ch.zhaw.parkship.user;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

  private final UserService userService;

  /**
  Retrieves a list of all users. Only consumable by users with ADMIN role.
  @return ResponseEntity with a list of UserDto objects in the body, or a no content response if the list is empty
  */
  @GetMapping(produces = "application/json")
  @Secured("ADMIN")
  public ResponseEntity<List<UserDto>> getAllUsers() {
    List<UserDto> userDtos = userService.getAll();
    if (userDtos.isEmpty()) {
      return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(userDtos);
  }
}
