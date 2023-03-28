package ch.zhaw.parkship.parking;

import java.io.Serializable;
import java.time.LocalDate;

import ch.zhaw.parkship.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto implements Serializable {
	private Long id;
	private ParkingLotDto parkingLot;
	private User tenant;
	private LocalDate from;
	private LocalDate to;

	public ReservationDto(ReservationEntity reservationEntity) {
		this.id = reservationEntity.getId();
		this.tenant = reservationEntity.getTenant();
		this.from = reservationEntity.getFrom();
		this.to = reservationEntity.getTo();
		this.parkingLot = new ParkingLotDto(reservationEntity.getParkingLot());
	}
}
