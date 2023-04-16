package ch.zhaw.parkship.util.generator;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.user.UserEntity;
import com.github.javafaker.Faker;

public abstract class ParkingLotGenerator {

    static Faker faker = new Faker();

    public static ParkingLotEntity generate(UserEntity owner) {
        var entity = new ParkingLotEntity();
        entity.setLongitude(Double.valueOf(faker.address().longitude()));
        entity.setLatitude(Double.valueOf(faker.address().latitude()));
        entity.setNr(faker.address().buildingNumber());
        entity.setPrice(faker.number().randomDouble(2, 10, 300));
        entity.setState(faker.address().state());
        entity.setAddress(faker.address().streetAddress());
        entity.setAddressNr(faker.address().streetAddressNumber());
        entity.setDescription(faker.weather().description());
        entity.setOwner(owner);
        return entity;
    }

    public static ParkingLotEntity generate(UserEntity owner, Long id) {
        var entity = generate(owner);
        entity.setId(id);
        return entity;
    }

}
