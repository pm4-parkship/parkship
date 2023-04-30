package ch.zhaw.parkship.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ch.zhaw.parkship.role.RoleDto;
import ch.zhaw.parkship.user.UserController;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserService;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
  private MockMvc mockMvc;

  @Mock
  private UserService userService;

  @InjectMocks
  private UserController userController;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  public void testGetAllUsers() throws Exception {
    List<UserDto> userList = new ArrayList<>();
    userList
        .add(new UserDto(1L, "test@test.com", "testUser", "Test", "User", new HashSet<RoleDto>()));
    when(userService.getAll()).thenReturn(userList);

    mockMvc.perform(get("/user").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id", is(1)))
        .andExpect(jsonPath("$[0].email", is("test@test.com")))
        .andExpect(jsonPath("$[0].username", is("testUser")))
        .andExpect(jsonPath("$[0].name", is("Test")))
        .andExpect(jsonPath("$[0].surname", is("User")))
        .andExpect(jsonPath("$[0].roles", hasSize(0)));
  }
}
