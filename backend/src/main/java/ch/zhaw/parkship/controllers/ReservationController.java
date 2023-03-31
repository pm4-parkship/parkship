package ch.zhaw.parkship.controllers;

import java.util.List;

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
		return reservationService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@PostMapping
	public ReservationDto createReservation(@RequestBody ReservationDto reservation) {
		try {
			return reservationService.create(reservation);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/{id}")
	public ReservationDto updateReservation(@PathVariable Long id, @RequestBody ReservationDto reservation) {
		try {
			return reservationService.update(id, reservation);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.CONFLICT);
		}
	}

	@DeleteMapping("/{id}")
	public void deleteReservation(@PathVariable Long id) {
		reservationService.delete(id);
	}

}