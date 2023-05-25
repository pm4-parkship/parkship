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
import ch.zhaw.parkship.user.UserState;

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
    @Profile({ "dev", "production" })
    @Transactional
    CommandLineRunner initTemplate(UserService userService, TagRepository tagRepository) {
        return args -> {
            UserEntity lukas = userService.signUp("Lukas", "Müller", "lukas.mueller@bluewin.ch", "pass123",
                    UserRole.USER);
            lukas.setName("Lukas");
            lukas.setSurname("Müller");
            lukas.setUserState(UserState.UNLOCKED);

            UserEntity nina = userService.signUp("Nina", "Schmid", "nina.schmid@gmx.ch", "secret456", UserRole.USER);
            nina.setName("Nina");
            nina.setSurname("Schmid");
            nina.setUserState(UserState.UNLOCKED);

            UserEntity simon = userService.signUp("Simon", "Brunner", "simon.brunner@sunrise.ch", "password789",
                    UserRole.USER);
            simon.setName("Simon");
            simon.setSurname("Brunner");
            simon.setUserState(UserState.UNLOCKED);

            UserEntity laura = userService.signUp("Laura", "Keller", "laura.keller@swissonline.ch", "securepass123",
                    UserRole.ADMIN);
            laura.setName("Laura");
            laura.setSurname("Keller");
            laura.setUserState(UserState.UNLOCKED);

            userService.save(lukas);
            userService.save(nina);
            userService.save(simon);
            userService.save(laura);

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
            TagEntity ueberwacht = new TagEntity();
            ueberwacht.setName("Überwacht");
            TagEntity niedrigEinfahrtsHoehe = new TagEntity();
            niedrigEinfahrtsHoehe.setName("Niedrige Einfahrtshöhe");
            TagEntity zugangskontrolle = new TagEntity();
            zugangskontrolle.setName("Zugangskontrolle");
            TagEntity nahverkehrsanbindung = new TagEntity();
            nahverkehrsanbindung.setName("Nahverkehrsanbindung");

            tagRepository.saveAll(List.of(ueberdacht, schatten, ladestation, barrierefrei, garage, ueberwacht,
                    niedrigEinfahrtsHoehe, zugangskontrolle, nahverkehrsanbindung));

            List<ParkingLotEntity> parkingLots = new ArrayList<>();
            var parkingLotMoTue = new ParkingLotEntity();
            parkingLotMoTue.setId(1L);
            parkingLotMoTue.getTags().add(ueberwacht);
            parkingLotMoTue.getTags().add(zugangskontrolle);
            parkingLotMoTue.setName("Überdacher Parkplatz mit Schatten");
            parkingLotMoTue.setLatitude(47.370349275337354);
            parkingLotMoTue.setLongitude(8.5389393156127);
            parkingLotMoTue.setNr("25");
            parkingLotMoTue.setAddress("Bahnhofstrasse");
            parkingLotMoTue.setPrice(150.0);
            parkingLotMoTue.setState(ParkingLotState.ACTIVE);
            parkingLotMoTue.setDescription(
                    "Überdachter Parkplatz mit Schatten in Zürichs Bahnhofstrasse. Ideal für Pendler und Besucher. 24/7 geöffnet, bietet Schutz vor Witterung und Komfort.");
            parkingLotMoTue.setOwner(laura);
            parkingLots.add(parkingLotMoTue);

            var parkingLotWorkdays = new ParkingLotEntity();
            parkingLotWorkdays.setId(2L);
            parkingLotWorkdays.getTags().add(ladestation);
            parkingLotWorkdays.getTags().add(ueberwacht);
            parkingLotWorkdays.getTags().add(zugangskontrolle);
            parkingLotWorkdays.setName("Elektro Parkplatz in Gehrenholzstrasse");
            parkingLotWorkdays.setLatitude(47.363893212583704);
            parkingLotWorkdays.setLongitude(8.507525639760503);
            parkingLotWorkdays.setNr("8");
            parkingLotWorkdays.setAddress("Gehrenholzstrasse");
            parkingLotWorkdays.setPrice(180.0);
            parkingLotWorkdays.setState(ParkingLotState.ACTIVE);
            parkingLotWorkdays.setDescription(
                    "Elektro-Parkplatz in der Gehrenholzstrasse mit Ladestationen. Ideal für umweltbewusste Pendler und Besucher. Schattig und sicher.");
            parkingLotWorkdays.setOwner(laura);
            parkingLots.add(parkingLotWorkdays);

            var parkingLotInactive = new ParkingLotEntity();
            parkingLotInactive.setId(3L);
            parkingLotInactive.getTags().add(ueberdacht);
            parkingLotInactive.getTags().add(nahverkehrsanbindung);
            parkingLotInactive.setName("Überdachter Parkplatz beim Wohnstadion Kirchenacker");
            parkingLotInactive.setLatitude(47.40983068220892);
            parkingLotInactive.setLongitude(8.5529563397625);
            parkingLotInactive.setNr("N/A");
            parkingLotInactive.setAddress("Wohnstadion Kirchenacker");
            parkingLotInactive.setPrice(160.0);
            parkingLotInactive.setState(ParkingLotState.INACTIVE);
            parkingLotInactive.setDescription(
                    "Überdachter Parkplatz beim Wohnstadion Kirchenacker. Ideal für sportliche und kulturelle Aktivitäten. Rund um die Uhr geöffnet, bietet Komfort und Sicherheit.");
            parkingLotInactive.setOwner(laura);
            parkingLots.add(parkingLotInactive);

            var parkingLotWeek = new ParkingLotEntity();
            parkingLotWeek.setId(4L);
            parkingLotWeek.getTags().add(ueberdacht);
            parkingLotWeek.getTags().add(schatten);
            parkingLotWeek.getTags().add(niedrigEinfahrtsHoehe);
            parkingLotWeek.setName("Überdachter Parkplatz in der Walter Mittelholzerstrasse");
            parkingLotWeek.setLatitude(47.44044693810441);
            parkingLotWeek.setLongitude(8.568585250628265);
            parkingLotWeek.setNr("8");
            parkingLotWeek.setAddress("Walter Mittelholzerstrasse");
            parkingLotWeek.setPrice(170.0);
            parkingLotWeek.setState(ParkingLotState.ACTIVE);
            parkingLotWeek.setDescription(
                    "Überdachter Parkplatz in der Walter Mittelholzerstrasse, Opfikon. Ideal für Pendler und Besucher. 24/7 geöffnet, bietet Schutz und Sicherheit.");
            parkingLotWeek.setOwner(lukas);
            parkingLots.add(parkingLotWeek);

            parkingLotRepository.saveAll(parkingLots);

            var offerMoTue = new OfferEntity();
            offerMoTue.setMonday(true);
            offerMoTue.setTuesday(true);
            offerMoTue.setWednesday(false);
            offerMoTue.setThursday(false);
            offerMoTue.setFriday(false);
            offerMoTue.setSaturday(false);
            offerMoTue.setSunday(false);
            offerMoTue.setFrom(LocalDate.of(2023, 7, 3));
            offerMoTue.setTo(LocalDate.of(2023, 7, 4));
            offerMoTue.setParkingLot(parkingLotMoTue);

            var offerWeek = new OfferEntity();
            offerWeek.setMonday(true);
            offerWeek.setTuesday(true);
            offerWeek.setWednesday(true);
            offerWeek.setThursday(true);
            offerWeek.setFriday(true);
            offerWeek.setSaturday(true);
            offerWeek.setSunday(true);
            offerWeek.setFrom(LocalDate.of(2023, 7, 3));
            offerWeek.setTo(LocalDate.of(2023, 10, 29));
            offerWeek.setParkingLot(parkingLotWeek);

            var offerWorkdays = new OfferEntity();
            offerWorkdays.setMonday(true);
            offerWorkdays.setTuesday(true);
            offerWorkdays.setWednesday(true);
            offerWorkdays.setThursday(true);
            offerWorkdays.setFriday(true);
            offerWorkdays.setSaturday(false);
            offerWorkdays.setSunday(false);
            offerWorkdays.setFrom(LocalDate.of(2023, 7, 18));
            offerWorkdays.setTo(LocalDate.of(2023, 8, 25));
            offerWorkdays.setParkingLot(parkingLotWorkdays);

            offerRepository.saveAll(List.of(offerMoTue, offerWeek, offerWorkdays));

            // create four reservations each two days long for offerWeek
            var reservationWeek1 = new ReservationEntity();
            reservationWeek1.setFrom(LocalDate.of(2023, 7, 3));
            reservationWeek1.setTo(LocalDate.of(2023, 7, 4));
            reservationWeek1.setParkingLot(parkingLotWeek);
            reservationWeek1.setTenant(parkingLotWeek.getOwner());
            reservationWeek1.setState(ReservationState.ACTIVE);

            var reservationWeek2 = new ReservationEntity();
            reservationWeek2.setFrom(LocalDate.of(2023, 8, 5));
            reservationWeek2.setTo(LocalDate.of(2023, 8, 6));
            reservationWeek2.setParkingLot(parkingLotWeek);
            reservationWeek2.setTenant(parkingLotWeek.getOwner());
            reservationWeek2.setState(ReservationState.ACTIVE);

            var reservationWeek3 = new ReservationEntity();
            reservationWeek3.setFrom(LocalDate.of(2023, 9, 17));
            reservationWeek3.setTo(LocalDate.of(2023, 9, 18));
            reservationWeek3.setParkingLot(parkingLotWeek);
            reservationWeek3.setTenant(parkingLotWeek.getOwner());
            reservationWeek3.setState(ReservationState.ACTIVE);

            var reservationWeek4 = new ReservationEntity();
            reservationWeek4.setFrom(LocalDate.of(2023, 10, 28));
            reservationWeek4.setTo(LocalDate.of(2023, 10, 29));
            reservationWeek4.setParkingLot(parkingLotWeek);
            reservationWeek4.setTenant(parkingLotWeek.getOwner());
            reservationWeek4.setState(ReservationState.ACTIVE);

            // create two reservations each three days long for offerWorkdays
            var reservationWorkdays1 = new ReservationEntity();
            reservationWorkdays1.setFrom(LocalDate.of(2023, 7, 18));
            reservationWorkdays1.setTo(LocalDate.of(2023, 7, 20));
            reservationWorkdays1.setParkingLot(parkingLotWorkdays);
            reservationWorkdays1.setTenant(parkingLotWorkdays.getOwner());
            reservationWorkdays1.setState(ReservationState.ACTIVE);

            var reservationWorkdays2 = new ReservationEntity();
            reservationWorkdays2.setFrom(LocalDate.of(2023, 8, 23));
            reservationWorkdays2.setTo(LocalDate.of(2023, 8, 25));
            reservationWorkdays2.setParkingLot(parkingLotWorkdays);
            reservationWorkdays2.setTenant(parkingLotWorkdays.getOwner());
            reservationWorkdays2.setState(ReservationState.ACTIVE);

            reservationRepository.saveAll(List.of(reservationWeek1, reservationWeek2, reservationWeek3,
                    reservationWeek4, reservationWorkdays1, reservationWorkdays2));
        };
    }
}
