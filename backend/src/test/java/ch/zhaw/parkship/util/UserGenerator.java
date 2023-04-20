package ch.zhaw.parkship.util;

import ch.zhaw.parkship.user.UserEntity;
import com.github.javafaker.Faker;

import java.util.Set;

public abstract class UserGenerator {
    static Faker faker = new Faker();

    public static UserEntity generate() {
        return generate(null);

    }

    public static UserEntity generate(Long id) {
        return new UserEntity(id,
                faker.internet().emailAddress(),
                faker.name().username(),
                faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().password(),
                Set.of(),
                Set.of(),
                Set.of());

    }


}