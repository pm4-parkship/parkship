package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.user.UserEntity;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "ParkingLot")
public class ParkingLotEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity owner;

    @Column
    private String description;

    @ManyToMany
    @JoinTable(name = "parking_lot_tag", joinColumns = @JoinColumn(name = "parking_lot_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<TagEntity> tags;

    @Column(nullable = false)
    private Double longitude;

    @Column(nullable = false)
    private Double latitude;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String addressNr;

    @Column(nullable = true)
    private Integer floor;

    @Column(nullable = true)
    private String nr;

    @Column(nullable = true)
    private byte[] picture;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false)
    private ParkingLotState state;

    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @JsonManagedReference
    private Set<ReservationEntity> reservationEntitySet;

    @OneToMany(mappedBy = "parkingLot", fetch = FetchType.LAZY, cascade = {CascadeType.ALL},
            orphanRemoval = true)
    @JsonManagedReference
    private Set<OfferEntity> offerEntitySet;

    public ParkingLotEntity() {
        this.tags = new HashSet<>();
    }

    @Override
    public String toString() {
        return "ParkingLotEntity{" + "id=" + id + ", owner=" + owner + ", description='" + description
                + '\'' + ", tags=" + tags + ", longitude=" + longitude + ", latitude=" + latitude
                + ", address='" + address + '\'' + ", addressNr='" + addressNr + '\'' + ", floor=" + floor
                + ", nr='" + nr + '\'' + ", price=" + price + ", state='" + state + '\''
                + ", reservationEntitySet=" + reservationEntitySet + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ParkingLotEntity))
            return false;
        ParkingLotEntity that = (ParkingLotEntity) o;
        return Objects.equals(id, that.id);
    }
}
