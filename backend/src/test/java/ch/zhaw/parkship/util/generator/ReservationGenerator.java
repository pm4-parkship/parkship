package ch.zhaw.parkship.util.generator;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationState;
import ch.zhaw.parkship.user.UserEntity;
import com.github.javafaker.Faker;

import java.time.LocalDate;

public abstract class ReservationGenerator {
    static Faker faker = new Faker();


    public static ReservationEntity generate(ParkingLotEntity parkingLotEntity, UserEntity tenant) {
        var reservation = new ReservationEntity();
        reservation.setFrom(LocalDate.now().plusDays(1));
        reservation.setTo(LocalDate.now().plusDays(2));
        reservation.setParkingLot(parkingLotEntity);
        reservation.setTenant(tenant);
        reservation.setState(ReservationState.ACTIVE);
        return reservation;
    }

    public static ReservationEntity generate(ParkingLotEntity parkingLotEntity, UserEntity tenant, Long id) {
        var reservation = generate(parkingLotEntity, tenant);
        reservation.setId(id);

        return reservation;
    }

}
