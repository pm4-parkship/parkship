package ch.zhaw.parkship.util;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotState;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.tag.TagRepository;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRole;
import ch.zhaw.parkship.user.UserService;
import ch.zhaw.parkship.user.UserState;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Locale;
import java.util.Random;

@Service
public class SeedGenerator {
    private static Faker faker = new Faker(new Locale("de-DE"));

    private static Random random = new Random();

    private final UserService userService;
    private final TagRepository tagRepository;
    private final ParkingLotRepository parkingLotRepository;


    public SeedGenerator(UserService userService, TagRepository tagRepository, ParkingLotRepository parkingLotRepository) {
        this.userService = userService;
        this.tagRepository = tagRepository;
        this.parkingLotRepository = parkingLotRepository;

    }

    public void generateSeeds(int numberOfSeeds) {
        for (int i = 0; i < numberOfSeeds; i++) {
            UserEntity user = generateUser();
            ParkingLotEntity parkingLot = generateParkingLot(user);
            generateOffer(parkingLot);
        }
    }

    private UserEntity generateUser() {
        UserEntity user = userService.signUp(faker.name().firstName(),
                faker.name().lastName(),
                faker.internet().emailAddress(),
                faker.internet().password(),
                UserRole.USER);
        user.setUserState(UserState.UNLOCKED);
        userService.save(user);
        return user;
    }


    private ParkingLotEntity generateParkingLot(UserEntity user) {
        ParkingLotEntity parkingLot = new ParkingLotEntity();
        parkingLot.getTags().add(getRandomTag());
        parkingLot.getTags().add(getRandomTag());
        parkingLot.getTags().add(getRandomTag());
        parkingLot.setName(faker.funnyName().name() + faker.name().bloodGroup());
        parkingLot.setLatitude(47.363893212583704);
        parkingLot.setLongitude(8.507525639760503);
        parkingLot.setNr(faker.address().streetAddressNumber());
        parkingLot.setAddress(faker.address().streetAddress());
        parkingLot.setAddressNr(faker.address().streetAddressNumber());
        parkingLot.setPrice(10.0);
        parkingLot.setState(ParkingLotState.ACTIVE);
        parkingLot.setDescription(faker.weather().description());
        parkingLot.setOwner(user);
        parkingLotRepository.save(parkingLot);
        return parkingLot;
    }

    private TagEntity getRandomTag() {
        int qty = tagRepository.findAll().size();
        Long index = random.nextLong(qty);
        return tagRepository.findById(index).get();
    }

    private void generateOffer(ParkingLotEntity parkingLotEntity) {
        OfferEntity offer = new OfferEntity();
        offer.setMonday(true);
        offer.setTuesday(true);
        offer.setWednesday(true);
        offer.setThursday(true);
        offer.setFriday(true);
        offer.setSaturday(true);
        offer.setSunday(true);
        offer.setFrom(LocalDate.of(2023, 7, 1));
        offer.setTo(LocalDate.of(2023, 12, 30));
        offer.setParkingLot(parkingLotEntity);
    }
}
