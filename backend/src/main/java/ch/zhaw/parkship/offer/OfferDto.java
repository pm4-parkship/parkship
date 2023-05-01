package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
public class OfferDto implements Serializable {
    private Long id;

    @NotNull
    private ParkingLotDto parkingLot;

    @NotNull
    private LocalDate from;

    @NotNull
    private LocalDate to;

    @NotNull
    private Boolean monday;

    @NotNull
    private Boolean tuesday;

    @NotNull
    private Boolean wednesday;

    @NotNull
    private Boolean thursday;

    @NotNull
    private Boolean friday;

    @NotNull
    private Boolean saturday;

    @NotNull
    private Boolean sunday;

    public OfferDto(OfferEntity offerEntity){
        this.id = offerEntity.getId();
        this.parkingLot = new ParkingLotDto(offerEntity.getParkingLot());
        this.from = offerEntity.getFrom();
        this.to = offerEntity.getTo();
        this.monday = offerEntity.getMonday();
        this.tuesday = offerEntity.getTuesday();
        this.wednesday = offerEntity.getWednesday();
        this.thursday = offerEntity.getThursday();
        this.friday = offerEntity.getFriday();
        this.saturday = offerEntity.getSaturday();
        this.sunday = offerEntity.getSunday();
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
