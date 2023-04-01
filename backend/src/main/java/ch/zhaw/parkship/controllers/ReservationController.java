package ch.zhaw.parkship.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import ch.zhaw.parkship.dtos.ReservationDto;
import ch.zhaw.parkship.services.ReservationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@PostMapping
	public ReservationDto createReservation(@Valid @RequestBody ReservationDto reservationDto) {
		return reservationService.create(reservationDto)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
	}

	@GetMapping("/{id}")
	public ReservationDto getReservationById(@PathVariable Long id) {
		return reservationService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping
	public List<ReservationDto> getAllReservations() {
		return reservationService.getAll();
	}

	@PutMapping("/{id}")
	public ReservationDto updateReservation(@PathVariable Long id, @Valid @RequestBody ReservationDto reservationDto) {
		reservationDto.setId(id);
		return reservationService.update(reservationDto)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
	}

	@DeleteMapping("/{id}")
	public void deleteReservationById(@PathVariable Long id) {
		var deleted = reservationService.deleteById(id);
		if (!deleted.isPresent()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
	}

}
