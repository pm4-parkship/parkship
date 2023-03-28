package ch.zhaw.parkship.parking;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.persistence.EntityNotFoundException;

@RestController
@RequestMapping("/parking-lot")
public class ParkingLotController {

	private final ParkingLotService parkingLotService;

	public ParkingLotController(ParkingLotService parkingLotService) {
		this.parkingLotService = parkingLotService;
	}

	@GetMapping
	public List<ParkingLotDto> getAllParkingLots() {
		return parkingLotService.readAll();
	}

	@GetMapping("/{id}")
	public ParkingLotDto getParkingLotById(@PathVariable Long id) {
		return parkingLotService.getById(id).orElseThrow(EntityNotFoundException::new);
	}
	
	@PostMapping("/{id}")
	public ParkingLotDto createParkingLot(@RequestBody ParkingLotDto parkingLot) {
		return parkingLotService.create(parkingLot);
	}
}