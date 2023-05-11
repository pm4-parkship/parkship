package ch.zhaw.parkship.util;

import ch.zhaw.parkship.parkinglot.ParkingLotState;
import ch.zhaw.parkship.user.UserRole;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public abstract class AbstractDataTest {

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ParkingLotRepository parkingLotRepository;

    @Autowired
    protected ReservationRepository reservationRepository;

    protected void doSeed() {

        UserEntity user = userService.signUp("user", "userSurname", "user@parkship.ch", "user",  UserRole.USER);
        UserEntity secondUser = userService.signUp("second", "secondSurname",  "second@parkship.ch","second",  UserRole.USER);
        UserEntity thirdUser = userService.signUp("thirdUser", "thirdUserSurname",  "thirdUser@parkship.ch","thirdUser",  UserRole.USER);
        UserEntity admin = userService.signUp("admin", "adminSurname",  "admin@parkship.ch","admin", UserRole.ADMIN);

        userService.save(user);
        userService.save(secondUser);
        userService.save(thirdUser);
        userService.save(admin);
        List<UserEntity> users = List.of(user, secondUser, thirdUser, admin);
        Faker faker = new Faker();

        List<ParkingLotEntity> parkingLots = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            var parkingLot = new ParkingLotEntity();
            parkingLot.setId(i + 1L);
            parkingLot.setName(faker.funnyName().name());
            parkingLot.setLongitude(Double.valueOf(faker.address().longitude()));
            parkingLot.setLatitude(Double.valueOf(faker.address().latitude()));
            parkingLot.setNr(faker.address().buildingNumber());
            parkingLot.setPrice(faker.number().randomDouble(2, 10, 300));
            parkingLot.setState(ParkingLotState.ACTIVE);
            parkingLot.setAddress(faker.address().streetAddress());
            parkingLot.setAddressNr(faker.address().streetAddressNumber());
            parkingLot.setDescription(faker.weather().description());
            parkingLot.setOwner(i == 0 ? admin : user);
            parkingLots.add(parkingLotRepository.save(parkingLot));
        }
        for (int i = 0; i < 9; i++) {
            var reservation = ReservationGenerator.generate(parkingLots.get(1), users.get(((i + 1) % 4)));
            reservationRepository.save(reservation);
        }
    }

}
