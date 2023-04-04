package ch.zhaw.parkship.reservation;

import java.io.Serializable;
import java.time.LocalDate;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservationDto implements Serializable {
	
	private Long id;
	
    @NotNull
    private ParkingLotDto parkingLot;
    
    @NotNull
    private UserDto tenant;
    
    @NotNull
    private LocalDate from;
    
    @NotNull
    private LocalDate to;

	public ReservationDto(ReservationEntity reservationEntity) {
		this.id = reservationEntity.getId();
		this.tenant = new UserDto(reservationEntity.getTenant());
		this.from = reservationEntity.getFrom();
		this.to = reservationEntity.getTo();
		this.parkingLot = new ParkingLotDto(reservationEntity.getParkingLot());
	}
	
	public ReservationDto() {}
}
