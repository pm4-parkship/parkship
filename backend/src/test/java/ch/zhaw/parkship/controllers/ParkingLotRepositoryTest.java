package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.util.AbstractDataRollbackTest;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.user.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
class ParkingLotRepositoryTest extends AbstractDataRollbackTest {

    UserEntity owner;
    UserEntity tenant1;
    UserEntity tenant2;
    ParkingLotEntity parkingLot;
    ReservationEntity reservationEntity1;
    ReservationEntity reservationEntity2;
    
    OfferEntity offerEntity1;
    OfferEntity offerEntity2;


    static class ReservationArgument implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) {
            return Stream.of(
                    Arguments.of(LocalDate.now(), LocalDate.now().plusDays(1), false),
                    Arguments.of(LocalDate.now().minusDays(1), LocalDate.now(), true),
                    Arguments.of(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1), false),
                    Arguments.of(LocalDate.now().minusDays(1), LocalDate.now().plusDays(20), false),
                    Arguments.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(11), false),
                    Arguments.of(LocalDate.now().plusDays(1), LocalDate.now().plusDays(10), true),
                    Arguments.of(LocalDate.now().plusDays(4), LocalDate.now().plusDays(7), true)
            );
        }
    }


    @ParameterizedTest
    @ArgumentsSource(ReservationArgument.class)
    @DisplayName("Test if the JPQL can select only parkinglots, which have no Reservation during given TimeSlot")
    void findFreeParkingLotsBetweenDates_hasTwoReservation(
            LocalDate from, LocalDate to, boolean shouldFind) {
        // act
        List<ParkingLotEntity> result = parkingLotRepository.findAvailableParkingLotsInRange(from, to);

        // assert
        Assertions.assertEquals(shouldFind ? 1 : 0, result.size());
    }

    @ParameterizedTest
    @ArgumentsSource(ReservationArgument.class)
    @DisplayName("Test if the JPQL can select only parkinglots, which have no Reservation during given TimeSlot")
    void isParkingLotAvailable_hasTwoReservation(
            LocalDate from, LocalDate to, boolean shouldFind) {
        // act
        ParkingLotEntity result = parkingLotRepository.isParkingLotAvailable(parkingLot, from, to);

        // assert
        if (shouldFind) {
            Assertions.assertNotNull(result);
        } else {
            Assertions.assertNull(result);
        }
    }
    
    void hasParkingLotOffer_offered() {
        LocalDate from = LocalDate.of(2023,5,1);
        LocalDate to = LocalDate.of(2023,5,2);

        // act
        ParkingLotEntity result = parkingLotRepository.isParkingLotOffered(parkingLot, from, to, true, true, false, false, false, false, false);

        // assert
        Assertions.assertNotNull(result);
    }

    void hasParkingLotOffer_notoffered() {
        LocalDate from = LocalDate.of(2024,5,1);
        LocalDate to = LocalDate.of(2024,5,2);

        // act
        ParkingLotEntity result = parkingLotRepository.isParkingLotOffered(parkingLot, from, to, true, true, false, false, false, false, false);

        // assert
        Assertions.assertNull(result);
    }
    


    @BeforeEach
    void init() {
        owner = UserGenerator.generate();
        tenant1 = UserGenerator.generate();
        tenant2 = UserGenerator.generate();
        parkingLot = ParkingLotGenerator.generate(owner);
        offerEntity1 = new OfferEntity();
        offerEntity2 = new OfferEntity();

        reservationEntity1 = ReservationGenerator.generate(parkingLot, tenant1);
        reservationEntity1.setFrom(LocalDate.now());
        reservationEntity1.setTo(LocalDate.now().plusDays(1));

        reservationEntity2 = ReservationGenerator.generate(parkingLot, tenant1);
        reservationEntity2.setFrom(LocalDate.now().plusDays(10));
        reservationEntity2.setTo(LocalDate.now().plusDays(12));

        offerEntity1.setFrom(LocalDate.of(2023,1,1));
        offerEntity1.setTo(LocalDate.of(2023,12,31));
        offerEntity1.setMonday(true);
        offerEntity1.setTuesday(true);
        offerEntity1.setWednesday(true);
        offerEntity1.setThursday(true);
        offerEntity1.setFriday(true);
        offerEntity1.setSaturday(true);
        offerEntity1.setSunday(true);
        offerEntity1.setParkingLot(parkingLot);

        offerEntity2.setFrom(LocalDate.of(2024,1,1));
        offerEntity2.setTo(LocalDate.of(2024,12,31));
        offerEntity2.setMonday(false);
        offerEntity2.setTuesday(false);
        offerEntity2.setWednesday(false);
        offerEntity2.setThursday(false);
        offerEntity2.setFriday(false);
        offerEntity2.setSaturday(false);
        offerEntity2.setSunday(false);
        offerEntity2.setParkingLot(parkingLot);
        
        userRepository.saveAll(List.of(owner, tenant1, tenant2));
        parkingLotRepository.save(parkingLot);
        reservationRepository.save(reservationEntity1);
        reservationRepository.save(reservationEntity2);
        offerRepository.save(offerEntity1);
        offerRepository.save(offerEntity2);
    }



}
