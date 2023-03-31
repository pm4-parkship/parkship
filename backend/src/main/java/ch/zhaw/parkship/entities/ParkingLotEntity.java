package ch.zhaw.parkship.entities;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "ParkingLot")
public class ParkingLotEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity owner;

	private String description;

	@ManyToMany
	@JoinTable(name = "parking_lot_tag",
            joinColumns = @JoinColumn(name = "parking_lot_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
	private Set<TagEntity> tags;

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
	
	public ParkingLotEntity() {
		this.tags = new HashSet<>();
	}
}
