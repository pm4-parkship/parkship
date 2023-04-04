package ch.zhaw.parkship.user;

import java.util.HashSet;
import java.util.Set;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.ReservationEntity;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

	@Column(nullable = false)
	private String password;

	@ElementCollection(fetch = FetchType.EAGER)
	private Set<String> roles;

	@Column(nullable = false, unique = true)
	private String email;

	@OneToMany(mappedBy = "tenant")
	private Set<ReservationEntity> reservations;

	@OneToMany(mappedBy = "owner")
	private Set<ParkingLotEntity> parkingLots;

	public UserEntity() {
		this.reservations = new HashSet<>();
		this.parkingLots = new HashSet<>();
	}

}
