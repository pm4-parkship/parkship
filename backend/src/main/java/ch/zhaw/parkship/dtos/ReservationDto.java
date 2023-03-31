package ch.zhaw.parkship.dtos;

import java.io.Serializable;
import java.time.LocalDate;

import ch.zhaw.parkship.entities.ReservationEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto implements Serializable {
	private Long id;
	private ParkingLotDto parkingLot;
	private String tenant;
	private LocalDate from;
	private LocalDate to;

	public ReservationDto(ReservationEntity reservationEntity) {
		this.id = reservationEntity.getId();
		this.tenant = reservationEntity.getTenant();
		this.from = reservationEntity.getFrom();
		this.to = reservationEntity.getTo();
		this.parkingLot = new ParkingLotDto(reservationEntity.getParkingLot());
	}
	
	public ReservationDto() {}
}
