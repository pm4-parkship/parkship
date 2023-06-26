package ch.zhaw.parkship.util;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.offer.OfferRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotState;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRole;
import ch.zhaw.parkship.user.UserService;
import ch.zhaw.parkship.user.UserState;
import com.github.javafaker.Faker;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Service
public class SeedGenerator {
    private static Faker faker = new Faker(new Locale("de-DE"));

    private static final Random random = new Random();

    private final UserService userService;
    private final ParkingLotRepository parkingLotRepository;
    private final OfferRepository offerRepository;


    public SeedGenerator(UserService userService, ParkingLotRepository parkingLotRepository, OfferRepository offerRepository) {
        this.userService = userService;
        this.parkingLotRepository = parkingLotRepository;
        this.offerRepository = offerRepository;
    }

    public void generateSeeds(int numberOfSeeds, List<TagEntity> tags) {
        for (int i = 0; i < numberOfSeeds; i++) {
            UserEntity user = generateUser();
            ParkingLotEntity parkingLot = generateParkingLot(user,tags);
            parkingLotRepository.save(parkingLot);

            OfferEntity offer = generateOffer(parkingLot);
            offerRepository.save(offer);

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


    private ParkingLotEntity generateParkingLot(UserEntity user, List<TagEntity> allTags) {
        ParkingLotEntity parkingLot = new ParkingLotEntity();

        parkingLot.getTags().add(getRandomTag(allTags));
        parkingLot.getTags().add(getRandomTag(allTags));
        parkingLot.getTags().add(getRandomTag(allTags));

        if (random.nextDouble() < 0.5){
            parkingLot.setName(faker.beer().name() + " " + faker.name().bloodGroup());
        }else{
            parkingLot.setName(faker.funnyName().name() + " " + faker.name().bloodGroup());
        }

        parkingLot.setLatitude(47.37773821639167 + random.nextFloat()/100);
        parkingLot.setLongitude(8.53285841502799 + random.nextFloat()/100);
        parkingLot.setAddress(faker.address().streetName());
        parkingLot.setAddressNr(faker.address().streetAddressNumber());
        parkingLot.setPrice(Double.valueOf(faker.commerce().price()));
        parkingLot.setState(ParkingLotState.ACTIVE);
        String description = faker.chuckNorris().fact() + "\n" + faker.shakespeare().romeoAndJulietQuote();
        parkingLot.setDescription(description.substring(0,Math.min(description.length(),254)));
        parkingLot.setOwner(user);
        return parkingLot;
    }

    private TagEntity getRandomTag(List<TagEntity> tags) {
        return tags.get(random.nextInt(tags.size()));
    }

    private OfferEntity generateOffer(ParkingLotEntity parkingLotEntity) {
        OfferEntity offer = new OfferEntity();
        offer.setMonday(true);
        offer.setTuesday(true);
        offer.setWednesday(true);
        offer.setThursday(true);
        offer.setFriday(true);
        offer.setSaturday(true);
        offer.setSunday(true);
        offer.setFrom(LocalDate.of(2023, 6, 1));
        offer.setTo(LocalDate.of(2023, 12, 30));
        offer.setParkingLot(parkingLotEntity);
        return offer;
    }
}
