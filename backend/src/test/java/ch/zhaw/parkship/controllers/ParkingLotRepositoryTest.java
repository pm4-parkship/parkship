package ch.zhaw.parkship.controllers;

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


    @BeforeEach
    void init() {
        owner = UserGenerator.generate();
        tenant1 = UserGenerator.generate();
        tenant2 = UserGenerator.generate();
        parkingLot = ParkingLotGenerator.generate(owner);

        reservationEntity1 = ReservationGenerator.generate(parkingLot, tenant1);
        reservationEntity1.setFrom(LocalDate.now());
        reservationEntity1.setTo(LocalDate.now().plusDays(1));

        reservationEntity2 = ReservationGenerator.generate(parkingLot, tenant1);
        reservationEntity2.setFrom(LocalDate.now().plusDays(10));
        reservationEntity2.setTo(LocalDate.now().plusDays(12));

        userRepository.saveAll(List.of(owner, tenant1, tenant2));
        parkingLotRepository.save(parkingLot);
        reservationRepository.save(reservationEntity1);
        reservationRepository.save(reservationEntity2);
    }



}
