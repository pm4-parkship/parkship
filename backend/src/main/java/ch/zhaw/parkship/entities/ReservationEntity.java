package ch.zhaw.parkship.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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
	private ParkingLotEntity parkingLot;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity tenant;

	@Column(nullable = false)
	private LocalDate from;

	@Column(nullable = false)
	private LocalDate to;
}
