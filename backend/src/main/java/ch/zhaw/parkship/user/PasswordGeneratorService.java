package ch.zhaw.parkship.user;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;

@NoArgsConstructor
@Service
public class PasswordGeneratorService {
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final Random RANDOM = new SecureRandom();
    private static final int PW_LENGTH = 10;

    public String generatePassword() {
        StringBuilder stringBuilder = new StringBuilder(PW_LENGTH);

        for (int i = 0; i < PW_LENGTH; i++) {
            stringBuilder.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return stringBuilder.toString();

    }

}
