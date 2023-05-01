package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "Offer")
public class OfferEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id", nullable = false)
    @JsonBackReference
    private ParkingLotEntity parkingLot;

    @Column(nullable = false)
    private LocalDate from;

    @Column(nullable = false)
    private LocalDate to;

    @Column(nullable = false)
    private Boolean monday;

    @Column(nullable = false)
    private Boolean tuesday;

    @Column(nullable = false)
    private Boolean wednesday;

    @Column(nullable = false)
    private Boolean thursday;

    @Column(nullable = false)
    private Boolean friday;

    @Column(nullable = false)
    private Boolean saturday;

    @Column(nullable = false)
    private Boolean sunday;
}
