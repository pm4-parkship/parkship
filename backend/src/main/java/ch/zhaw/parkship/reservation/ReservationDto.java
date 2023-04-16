package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.user.UserDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class ReservationDto implements Serializable {

    private Long id;

    @NotNull
    private ParkingLotDto parkingLot;

    @NotNull
    private UserDto tenant;

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;

    public ReservationDto(ReservationEntity reservationEntity) {
        this.id = reservationEntity.getId();
        this.tenant = new UserDto(reservationEntity.getTenant());
        this.from = reservationEntity.getFrom();
        this.to = reservationEntity.getTo();
        this.parkingLot = new ParkingLotDto(reservationEntity.getParkingLot());
    }

    public ReservationDto() {
    }

    @Override
    public String toString() {
        return "ReservationDto{" + "id=" + id + ", parkingLot=" + parkingLot + ", tenant=" + tenant
                + ", from=" + from + ", to=" + to + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ReservationDto))
            return false;
        ReservationDto that = (ReservationDto) o;
        return Objects.equals(id, that.id);
    }
}
