package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "Reservation")
public class ReservationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    @JsonBackReference
    private ParkingLotEntity parkingLot;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity tenant;

    @Column(nullable = false, name = "\"from\"")
    private LocalDate from;

    @Column(nullable = false)
    private LocalDate to;

    @Column(nullable = false)
    private ReservationState state;

    private LocalDate cancelDate;

    @Override
    public String toString() {
        return "ReservationEntity{" + "id=" + id + ", parkingLot=" + parkingLot + ", tenant=" + tenant
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
        if (!(o instanceof ReservationEntity))
            return false;
        ReservationEntity that = (ReservationEntity) o;
        return Objects.equals(id, that.id);
    }
}
