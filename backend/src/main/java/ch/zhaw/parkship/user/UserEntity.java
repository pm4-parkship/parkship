package ch.zhaw.parkship.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import ch.zhaw.parkship.authentication.ApplicationUser;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.ReservationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "\"User\"")
public class UserEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String surname;

  @OneToMany(mappedBy = "tenant")
  private Set<ReservationEntity> reservations;

  @OneToMany(mappedBy = "owner")
  private Set<ParkingLotEntity> parkingLots;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private ApplicationUser applicationUser;

  public UserEntity(String name, String surname) {
    this.name = name;
    this.surname = surname;
    this.reservations = new HashSet<>();
    this.parkingLots = new HashSet<>();
  }

  public UserEntity() {
    this.reservations = new HashSet<>();
    this.parkingLots = new HashSet<>();
  }

  @Override
  public String toString() {
    return "UserEntity{" + "id=" + id + ", name='" + name + '\'' + ", surname='" + surname + '\''
        + ", reservations=" + reservations + ", parkingLots=" + parkingLots + ", applicationUser="
        + applicationUser + '}';
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (!(o instanceof UserEntity))
      return false;
    UserEntity user = (UserEntity) o;
    return Objects.equals(id, user.id);
  }

}
