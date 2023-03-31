package ch.zhaw.parkship.entities;

import java.util.Set;

import jakarta.persistence.CascadeType;
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

@Entity
@Getter
@Setter
@Table(name = "ParkingLot")
public class ParkingLotEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String owner;

	private String description;

	@ElementCollection
	private Set<String> tags;

	@Column(nullable = false)
	private Double longitude;

	@Column(nullable = false)
	private Double latitude;

	private String address;

	private String addressNr;

	private Integer floor;

	@Column(nullable = false)
	private String nr;

	private byte[] picture;

	@Column(nullable = false)
	private Double price;

	@Column(nullable = false)
	private String state;

	@OneToMany(mappedBy = "parkingLot", fetch = FetchType.EAGER, cascade = {
			CascadeType.ALL }, orphanRemoval = true)
	private Set<ReservationEntity> reservationEntitySet;
}
