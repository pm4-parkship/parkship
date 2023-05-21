package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.user.*;
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
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private PasswordGeneratorService passwordGeneratorService;

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
                .add(new UserDto(1L, "test@test.com", "Test", "User", UserRole.USER, UserState.UNLOCKED));
        when(userService.getAll()).thenReturn(userList);

        mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("test@test.com")))
                .andExpect(jsonPath("$[0].name", is("Test")))
                .andExpect(jsonPath("$[0].surname", is("User")))
                .andExpect(jsonPath("$[0].role", is("USER")));
    }

    @Test
    void testSignUp() {
        String password = "1231231233";
        //New user
        when(passwordGeneratorService.generatePassword()).thenReturn(password);
        when(userService.existsByUsername("test@test.ch")).thenReturn(false);
        UserEntity user = new UserEntity("test@test.ch", password);
        user.setId(1L);
        user.setUserRole(UserRole.USER);
        user.setUserState(UserState.UNLOCKED);
        when(userService.signUp("tester", "testerSurname", "test@test.ch", password, UserRole.USER)).thenReturn(user);
        UserController.SignUpRequestDTO signUpRequestDTO = new UserController.SignUpRequestDTO("tester", "testerSurname", "test@test.ch", UserRole.USER);
        UserController.SignUpResponseDTO signUpResponseDTO = userController.signUp(signUpRequestDTO);

        assertNotNull(signUpResponseDTO, "SignUpResponseDTO is null");
        assertEquals(1L, signUpResponseDTO.id(), "User id in response is null");
        assertEquals(password, signUpResponseDTO.password(), "User password in response is null");
        assertEquals("test@test.ch", signUpResponseDTO.username(), "Username in response is not correct");
        assertEquals(UserRole.USER, signUpResponseDTO.userRole(), "Username in response is not correct");

        //Email already exists
        when(userService.existsByUsername("user@parkship.ch")).thenReturn(true);
        UserController.SignUpRequestDTO signUpRequestDTOEmailExists = new UserController.SignUpRequestDTO("user", "userSurname", "user@parkship.ch",  UserRole.USER);

        assertThrows(ResponseStatusException.class, () -> {
            userController.signUp(signUpRequestDTOEmailExists);
        }, "No exception thrown, because email already exists");

        //Username already exists
        when(userService.existsByUsername("neu@neu.ch")).thenReturn(true);
        UserController.SignUpRequestDTO signUpRequestDTOUsernameExists = new UserController.SignUpRequestDTO("neu","neuSurname","neu@neu.ch",  UserRole.USER);

        assertThrows(ResponseStatusException.class, () -> {
            userController.signUp(signUpRequestDTOUsernameExists);
        }, "No exception thrown, because username already exists");
    }


}
