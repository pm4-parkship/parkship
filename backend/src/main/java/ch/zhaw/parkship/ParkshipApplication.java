package ch.zhaw.parkship;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.offer.OfferRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.reservation.ReservationState;
import ch.zhaw.parkship.role.RoleEntity;
import ch.zhaw.parkship.role.RoleRepository;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserService;
import com.github.javafaker.Faker;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final OfferRepository offerRepository;


    public ParkshipApplication(ParkingLotRepository parkingLotRepository,
                               ReservationRepository reservationRepository,
                               OfferRepository offerRepository) {
        this.parkingLotRepository = parkingLotRepository;
        this.reservationRepository = reservationRepository;
        this.offerRepository = offerRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(ParkshipApplication.class, args);
    }

    @Bean
    @Profile({"dev", "production"})
    @Transactional
    CommandLineRunner initTemplate(@Autowired RoleRepository roleRepository,
                                   UserService userService) {
        return args -> {
            Faker faker = new Faker();
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

            List<UserEntity> users = List.of(user, secondUser, thirdUser, admin);

            for(UserEntity u : users){
                u.setName(faker.name().firstName());
                u.setSurname(faker.name().lastName());
                userService.save(u);
            }

            List<ParkingLotEntity> parkingLots = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                var parkingLot = new ParkingLotEntity();
                parkingLot.setId(i + 1L);
                parkingLot.setLongitude(Double.valueOf(faker.address().longitude()));
                parkingLot.setLatitude(Double.valueOf(faker.address().latitude()));
                parkingLot.setNr(faker.address().buildingNumber());
                parkingLot.setPrice(faker.number().randomDouble(2, 10, 300));
                parkingLot.setState(faker.address().state());
                parkingLot.setAddress(faker.address().streetName());
                parkingLot.setAddressNr(faker.address().streetAddressNumber());
                parkingLot.setDescription(faker.hitchhikersGuideToTheGalaxy().quote());
                parkingLot.setOwner(i == 0 ? admin : user);
                parkingLotRepository.save(parkingLot);
                parkingLots.add(parkingLot);
            }

            for(ParkingLotEntity lot : parkingLots){
                var offer = new OfferEntity();
                offer.setFrom(LocalDate.of(2023,1,1));
                offer.setTo(LocalDate.of(2023,12,31));
                offer.setMonday(faker.random().nextBoolean());
                offer.setTuesday(faker.random().nextBoolean());
                offer.setWednesday(faker.random().nextBoolean());
                offer.setThursday(faker.random().nextBoolean());
                offer.setFriday(faker.random().nextBoolean());
                offer.setSaturday(faker.random().nextBoolean());
                offer.setSunday(faker.random().nextBoolean());
                offer.setParkingLot(lot);
                offerRepository.save(offer);
            }

            for (int i = 0; i < 9; i++) {
                var reservation = new ReservationEntity();
                Date current = new Date();
                reservation.setFrom(LocalDate.of(current.getYear()+1900, current.getMonth()+1, current.getDay()+1+i));
                reservation.setTo(LocalDate.of(current.getYear()+1900, current.getMonth()+1, current.getDay()+1+i+faker.random().nextInt(0,2)));
                reservation.setParkingLot(parkingLots.get((i + 1) % 5));
                reservation.setTenant(users.get(((i + 1) % 4)));
                reservation.setState(ReservationState.CANCELED);
                reservationRepository.save(reservation);
            }
        };
    }

}
