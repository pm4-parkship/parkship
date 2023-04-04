package ch.zhaw.parkship.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ch.zhaw.parkship.dtos.ReservationDto;
import ch.zhaw.parkship.services.ReservationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

	@Autowired
	private ReservationService reservationService;

	@PostMapping(consumes = "application/json", produces = "application/json")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<ReservationDto> createReservation(@Valid @RequestBody ReservationDto ReservationDto) {
		Optional<ReservationDto> createdReservation = reservationService.create(ReservationDto);
		return createdReservation.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
		Optional<ReservationDto> reservationDto = reservationService.getById(id);
		if (reservationDto.isPresent()) {
			return ResponseEntity.ok(reservationDto.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<ReservationDto>> getAllReservations() {
		List<ReservationDto> reservationDtos = reservationService.getAll();
		if (reservationDtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(reservationDtos);
	}

	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ReservationDto> updateReservation(@PathVariable Long id,
			@Valid @RequestBody ReservationDto reservationDto) {
		reservationDto.setId(id);
		Optional<ReservationDto> updatedReservation = reservationService.update(reservationDto);
		if (updatedReservation.isPresent()) {
			return ResponseEntity.ok(updatedReservation.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
		Optional<ReservationDto> deletedReservation = reservationService.deleteById(id);
		if (deletedReservation.isPresent()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}

}
