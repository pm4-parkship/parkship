package ch.zhaw.parkship.parking;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.parkship.parking.dto.ReservationDto;
import ch.zhaw.parkship.parking.service.ReservationService;
import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	private final ReservationService reservationService;

	public ReservationController(ReservationService reservationService) {
		this.reservationService = reservationService;
	}

	@GetMapping
	public List<ReservationDto> getAllReservations() {
		return reservationService.readAll();
	}

	@GetMapping("/{id}")
	public ReservationDto getReservationById(@PathVariable Long id) {
		return reservationService.getById(id)
				.orElseThrow(() -> new EntityNotFoundException("Parking lot not found with id: " + id));
	}

	@PostMapping
	public ReservationDto createReservation(@RequestBody ReservationDto reservation) {
		return reservationService.create(reservation);
	}

	@PutMapping("/{id}")
	public ReservationDto updateReservation(@PathVariable Long id, @RequestBody ReservationDto reservation) {
		return reservationService.update(id, reservation);
	}

	@DeleteMapping("/{id}")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.delete(id);
	}

}