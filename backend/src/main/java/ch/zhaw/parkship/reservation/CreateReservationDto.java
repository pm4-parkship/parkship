package ch.zhaw.parkship.reservation;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.user.UserEntity;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public record CreateReservationDto(
        @NotNull long parkingLotID,
        @NotNull LocalDate from,
        @NotNull LocalDate to) implements Serializable {

    @Override
    public String toString() {
        return "CreateReservationDto{" + "parkingLotid=" + parkingLotID
                + ", from=" + from + ", to=" + to + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingLotID, from.hashCode(), to.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof CreateReservationDto that))
            return false;
        return Objects.equals(parkingLotID, that.parkingLotID) && from.isEqual(that.from) && to.equals(that.to);
    }
}
