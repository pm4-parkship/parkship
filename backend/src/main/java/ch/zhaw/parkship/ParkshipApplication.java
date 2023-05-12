package ch.zhaw.parkship;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.offer.OfferRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotState;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.reservation.ReservationState;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.tag.TagRepository;
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
    CommandLineRunner initTemplate(UserService userService, TagRepository tagRepository) {
        return args -> {
            Faker faker = new Faker();

            UserEntity user = userService.signUp("user", "userSurname", "user@parkship.ch", "user", UserRole.USER);
            user.setName(faker.name().lastName());
            user.setSurname(faker.name().firstName());
            UserEntity secondUser = userService.signUp("second", "secondSurname", "second@parkship.ch", "second", UserRole.USER);
            secondUser.setName(faker.name().lastName());
            secondUser.setSurname(faker.name().firstName());
            UserEntity thirdUser = userService.signUp("thirdUser", "thirdUserSurname", "thirdUser@parkship.ch", "thirdUser", UserRole.USER);
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


            TagEntity ueberdacht = new TagEntity();
            ueberdacht.setName("überdacht");
            TagEntity schatten = new TagEntity();
            schatten.setName("im Schatten");
            TagEntity ladestation = new TagEntity();
            ladestation.setName("Ladestation");
            TagEntity barrierefrei = new TagEntity();
            barrierefrei.setName("barrierefrei");
            TagEntity garage = new TagEntity();
            garage.setName("Garage");

            tagRepository.saveAll(List.of(ueberdacht, schatten, ladestation, barrierefrei, garage));



            List<ParkingLotEntity> parkingLots = new ArrayList<>();

            for (int i = 0; i < 5; i++) {
                var parkingLot = new ParkingLotEntity();
                parkingLot.setId(i + 1L);
                parkingLot.getTags().add(i == 0 ? barrierefrei : garage);
                parkingLot.setName(faker.funnyName().name());
                parkingLot.setLongitude(Double.valueOf(faker.address().longitude()));
                parkingLot.setLatitude(Double.valueOf(faker.address().latitude()));
                parkingLot.setNr(faker.address().buildingNumber());
                parkingLot.setPrice(faker.number().randomDouble(2, 10, 300));
                parkingLot.setState(ParkingLotState.ACTIVE);
                parkingLot.setAddress(faker.address().streetName());
                parkingLot.setAddressNr(faker.address().streetAddressNumber());
                String description = faker.hitchhikersGuideToTheGalaxy().quote();
                parkingLot.setDescription(description.length() > 255 ? description.substring(0,255) : description);
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
                reservation.setState(ReservationState.ACTIVE);
                reservationRepository.save(reservation);
            }
            for (int i = 0; i < 9; i++) {
                var reservation = new ReservationEntity();
                Date current = new Date();
                reservation.setFrom(LocalDate.of(current.getYear()+1900, current.getMonth()+1, current.getDay()+1+i));
                reservation.setTo(LocalDate.of(current.getYear()+1900, current.getMonth()+1, current.getDay()+1+i+faker.random().nextInt(0,2)));
                reservation.setParkingLot(parkingLots.get((i + 1) % 5));
                reservation.setTenant(users.get(((i + 1) % 4)));
                reservation.setState(ReservationState.CANCELED);
                reservation.setCancelDate(LocalDate.of(current.getYear() + 1900, current.getMonth() + 1, current.getDay() + 1));
                reservationRepository.save(reservation);
            }




        };
    }

}
