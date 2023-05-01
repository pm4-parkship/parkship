package ch.zhaw.parkship;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotState;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.reservation.ReservationState;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRole;
import ch.zhaw.parkship.user.UserService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class ParkshipApplication {
    private final ParkingLotRepository parkingLotRepository;
    private final ReservationRepository reservationRepository;


    public ParkshipApplication(ParkingLotRepository parkingLotRepository,
                               ReservationRepository reservationRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.reservationRepository = reservationRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkshipApplication.class, args);
    }

    @Bean
    @Profile({"dev", "production"})
    @Transactional
    CommandLineRunner initTemplate(UserService userService) {
        return args -> {
            Faker faker = new Faker();

            UserEntity user = userService.signUp("user", "userSurname",  "user@parkship.ch", "user", UserRole.USER);
            user.setName(faker.name().lastName());
            user.setSurname(faker.name().firstName());
            UserEntity secondUser = userService.signUp("second", "secondSurname",  "second@parkship.ch", "second", UserRole.USER);
            secondUser.setName(faker.name().lastName());
            secondUser.setSurname(faker.name().firstName());
            UserEntity thirdUser = userService.signUp("thirdUser", "thirdUserSurname",  "thirdUser@parkship.ch", "thirdUser", UserRole.USER);
            thirdUser.setName(faker.name().lastName());
            thirdUser.setSurname(faker.name().firstName());
            UserEntity admin = userService.signUp("admin", "adminSurname", "admin@parkship.ch","admin", UserRole.ADMIN);
            admin.setName(faker.name().lastName());
            admin.setSurname(faker.name().firstName());


            //admin.setUserRole(UserRole.ADMIN);
            userService.save(user);
            userService.save(secondUser);
            userService.save(thirdUser);
            userService.save(admin);
            List<UserEntity> users = List.of(user, secondUser, thirdUser, admin);


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
                parkingLotRepository.save(parkingLot);
                parkingLots.add(parkingLot);
            }

            for (int i = 0; i < 9; i++) {
                var reservation = new ReservationEntity();
                Date from = faker.date().future(2, 1, TimeUnit.DAYS);
                reservation.setFrom(LocalDate.of(from.getYear() + 1900, from.getMonth() + 1, from.getDay() + 1));
                Date to = faker.date().future(2, 1, TimeUnit.DAYS);
                reservation.setTo(LocalDate.of(to.getYear() + 1900, to.getMonth() + 1, to.getDay() + 1));
                reservation.setParkingLot(parkingLots.get((i + 1) % 5));
                reservation.setTenant(users.get(((i + 1) % 4)));
                reservation.setState(ReservationState.ACTIVE);
                reservationRepository.save(reservation);
            }
            for (int i = 0; i < 9; i++) {
                var reservation = new ReservationEntity();
                Date from = faker.date().future(2, 1, TimeUnit.DAYS);
                reservation.setFrom(LocalDate.of(from.getYear() + 1900, from.getMonth() + 1, from.getDay() + 1));
                Date to = faker.date().future(2, 1, TimeUnit.DAYS);
                reservation.setTo(LocalDate.of(to.getYear() + 1900, to.getMonth() + 1, to.getDay() + 1));
                reservation.setParkingLot(parkingLots.get((i + 1) % 5));
                reservation.setTenant(users.get(((i + 1) % 4)));
                reservation.setState(ReservationState.CANCELED);
                reservation.setCancelDate(LocalDate.of(from.getYear() + 1900, from.getMonth() + 1, from.getDay() + 1));
                reservationRepository.save(reservation);
            }
        };
    }

}
