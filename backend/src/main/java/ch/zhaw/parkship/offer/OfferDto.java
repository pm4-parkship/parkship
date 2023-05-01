package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.reservation.ReservationDto;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class OfferDto {
    private Long id;

    @NotNull
    private ParkingLotDto parkingLot;

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;

    @NotNull
    private boolean monday;

    @NotNull
    private boolean tuesday;

    @NotNull
    private boolean wednesday;

    @NotNull
    private boolean thursday;

    @NotNull
    private boolean friday;

    @NotNull
    private boolean saturday;

    @NotNull
    private boolean sunday;

    public OfferDto(OfferEntity offerEntity){
        this.id = offerEntity.getId();
        this.parkingLot = new ParkingLotDto(offerEntity.getParkingLot());
        this.from = offerEntity.getFrom();
        this.to = offerEntity.getTo();
        this.monday = offerEntity.isMonday();
        this.tuesday = offerEntity.isTuesday();
        this.wednesday = offerEntity.isWednesday();
        this.thursday = offerEntity.isThursday();
        this.friday = offerEntity.isFriday();
        this.saturday = offerEntity.isSaturday();
        this.sunday = offerEntity.isSunday();
    }

    @Override
    public String toString() {
        return "ReservationDto{" + "id=" + id + ", parkingLot=" + parkingLot + ", from=" + from + ", to=" + to +
                ", monday" + monday + ", tuesday" + tuesday + ", wednesday" + wednesday + ", thursday" + thursday +
                ", friday" + friday + ", saturday" + saturday + ", sunday" + sunday + '}';
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof OfferDto))
            return false;
        OfferDto that = (OfferDto) o;
        return Objects.equals(id, that.id);
    }

}
