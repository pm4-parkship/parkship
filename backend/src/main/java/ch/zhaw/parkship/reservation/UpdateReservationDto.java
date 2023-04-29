package ch.zhaw.parkship.reservation;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;


public record UpdateReservationDto(
        @NotNull long parkingLotID,
        long reservationID,
        @NotNull LocalDate from,
        @NotNull LocalDate to) implements Serializable {

    @Override
    public String toString() {
        return "UpdateReservationDto{" + "parkingLotid=" + parkingLotID + "reservationID" + reservationID
                + ", from=" + from + ", to=" + to + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(parkingLotID, reservationID, from.hashCode(), to.hashCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UpdateReservationDto that))
            return false;
        return Objects.equals(parkingLotID, that.parkingLotID) && reservationID == that.reservationID && from.isEqual(that.from) && to.equals(that.to);
    }
}
