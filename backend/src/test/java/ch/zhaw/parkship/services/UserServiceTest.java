package ch.zhaw.parkship.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  public void testGetAllUsers() {
    List<UserEntity> userList = new ArrayList<>();
    var userEntity = new UserEntity("test@test.com", "password");
    userEntity.setId(1L);
    userEntity.setName("Test");
    userEntity.setSurname("User");
    userList.add(userEntity);

    when(userRepository.findAll()).thenReturn(userList);

    List<UserDto> userDtoList = userService.getAll();
    assertEquals(1, userDtoList.size());
    assertEquals(1L, userDtoList.get(0).id().longValue());
    assertEquals("test@test.com", userDtoList.get(0).email());
    assertEquals("Test", userDtoList.get(0).name());
    assertEquals("User", userDtoList.get(0).surname());
  }


}
