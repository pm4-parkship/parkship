package ch.zhaw.parkship.parkinglot;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/parking-lot")
public class ParkingLotController {

	private ParkingLotService parkingLotService;

	public ParkingLotController(ParkingLotService parkingLotService) {
		this.parkingLotService = parkingLotService;
	}

	@PostMapping(consumes = "application/json", produces = "application/json")
	public ResponseEntity<ParkingLotDto> createParkingLot(@Valid @RequestBody ParkingLotDto parkingLotDto) {
		Optional<ParkingLotDto> createdParkingLot = parkingLotService.create(parkingLotDto);
		return createdParkingLot.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
				.orElseGet(() -> ResponseEntity.badRequest().build());
	}

	@GetMapping(value = "/{id}", produces = "application/json")
	public ResponseEntity<ParkingLotDto> getParkingLotById(@PathVariable Long id) {
		Optional<ParkingLotDto> parkingLotDto = parkingLotService.getById(id);
		if (parkingLotDto.isPresent()) {
			return ResponseEntity.ok(parkingLotDto.get());
		}
		return ResponseEntity.notFound().build();
	}

	@GetMapping(produces = "application/json")
	public ResponseEntity<List<ParkingLotDto>> getAllParkingLots() {
		List<ParkingLotDto> parkingLotDtos = parkingLotService.getAll();
		if (parkingLotDtos.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(parkingLotDtos);
	}

	@PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
	public ResponseEntity<ParkingLotDto> updateParkingLot(@PathVariable Long id,
			@Valid @RequestBody ParkingLotDto parkingLotDto) {
		parkingLotDto.setId(id);
		Optional<ParkingLotDto> updatedParkingLot = parkingLotService.update(parkingLotDto);
		if (updatedParkingLot.isPresent()) {
			return ResponseEntity.ok(updatedParkingLot.get());
		}
		return ResponseEntity.notFound().build();
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteParkingLot(@PathVariable Long id) {
		Optional<ParkingLotDto> deletedParkingLot = parkingLotService.deleteById(id);
		if (deletedParkingLot.isPresent()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.notFound().build();
	}
}
