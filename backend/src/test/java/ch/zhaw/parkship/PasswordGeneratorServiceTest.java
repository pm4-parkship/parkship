package ch.zhaw.parkship;

import ch.zhaw.parkship.user.PasswordGeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class PasswordGeneratorServiceTest {

    @Autowired
    private PasswordGeneratorService passwordGeneratorService;

    @Test
    void testGeneratePassword() {
        String password = passwordGeneratorService.generatePassword();
        assertEquals(10, password.length());

        String password2 = passwordGeneratorService.generatePassword();
        assertNotEquals(password, password2);
    }


}
