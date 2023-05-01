package ch.zhaw.parkship.util;

import ch.zhaw.parkship.offer.OfferRepository;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.role.RoleRepository;
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
    protected RoleRepository roleRepository;

    @Autowired
    protected UserService userService;

    @Autowired
    protected ParkingLotRepository parkingLotRepository;

    @Autowired
    protected ReservationRepository reservationRepository;

    @Autowired
    protected OfferRepository offerRepository;

    protected void doSeed() {
        if (roleRepository.findByName("ADMIN") != null) {
            return;
        }

        RoleEntity userRoleEntity = new RoleEntity("USER");
        RoleEntity adminRoleEntity = new RoleEntity("ADMIN");

        roleRepository.saveAll(Set.of(userRoleEntity, adminRoleEntity));

        UserEntity user = userService.signUp("user", "user@parkship.ch", "user");
        UserEntity secondUser = userService.signUp("second", "second@parkship.ch", "second");
        UserEntity thirdUser = userService.signUp("thirdUser", "thirdUser@parkship.ch", "thirdUser");
        UserEntity admin = userService.signUp("admin", "admin@parkship.ch", "admin");

        user.getRoleEntities().add(userRoleEntity);
        secondUser.getRoleEntities().add(userRoleEntity);
        thirdUser.getRoleEntities().add(userRoleEntity);
        admin.getRoleEntities().add(adminRoleEntity);

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
            parkingLot.setLongitude(Double.valueOf(faker.address().longitude()));
            parkingLot.setLatitude(Double.valueOf(faker.address().latitude()));
            parkingLot.setNr(faker.address().buildingNumber());
            parkingLot.setPrice(faker.number().randomDouble(2, 10, 300));
            parkingLot.setState(faker.address().state());
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
