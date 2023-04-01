package ch.zhaw.parkship.controllers;

import java.util.List;

import ch.zhaw.parkship.dtos.ParkingLotSearchDto;
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

import ch.zhaw.parkship.dtos.ParkingLotDto;
import ch.zhaw.parkship.services.ParkingLotService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/parking-lots")
public class ParkingLotController {

	@Autowired
	private ParkingLotService parkingLotService;

	@PostMapping
	public ParkingLotDto createParkingLot(@Valid @RequestBody ParkingLotDto parkingLotDto) {
		var createdParkingLot = parkingLotService.create(parkingLotDto);
		if (createdParkingLot.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
		}
		return createdParkingLot.get();
	}

	@GetMapping("/{id}")
	public ParkingLotDto getParkingLotById(@PathVariable Long id) {
		var parkingLotOptional = parkingLotService.getById(id);
		return parkingLotOptional
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking lot not found"));
	}

	@GetMapping
	public List<ParkingLotDto> getAllParkingLots() {
		return parkingLotService.getAll();
	}

	@PutMapping("/{id}")
	public ParkingLotDto updateParkingLot(@PathVariable Long id, @Valid @RequestBody ParkingLotDto parkingLotDto) {
		parkingLotDto.setId(id);
		var updatedParkingLotOptional = parkingLotService.update(parkingLotDto);
		return updatedParkingLotOptional
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking lot not found"));
	}

	@DeleteMapping("/{id}")
	public void deleteParkingLot(@PathVariable Long id) {
		var deleted = parkingLotService.deleteById(id);
		if (deleted.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Parking lot not found");
		}
	}

	@PostMapping("/searchTerm")
	public List<ParkingLotDto> searchParkingLot(@RequestBody ParkingLotSearchDto parkingLotSearchDto){
		return parkingLotService.getBySearchTerm(parkingLotSearchDto);
	}

}
