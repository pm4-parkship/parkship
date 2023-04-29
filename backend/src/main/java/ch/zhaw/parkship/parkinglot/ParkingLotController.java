package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.user.UserEntity;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * This class is a Rest Controller for managing ParkingLotDto objects
 * <p>
 * and exposes various API end-points for CRUD operations.
 */
@RestController
@RequestMapping("/parking-lot")
@SecurityRequirement(name = "Bearer Authentication")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;
    private final String DEFAULT_PAGE_NUM = "0";
    private final String DEFAULT_PAGE_SIZE = "100";

    /**
     * This end-point creates a new parking lot with the provided parking lot data.
     *
     * @param parkingLotDto The parking lot data to be saved.
     * @return ResponseEntity<ParkingLotDto> Returns the saved parking lot data in the HTTP response
     * body with a status code of 201 if created successfully, otherwise returns a bad request
     * status code.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParkingLotDto> createParkingLot(
            @Valid @RequestBody ParkingLotDto parkingLotDto) {
        Optional<ParkingLotDto> createdParkingLot = parkingLotService.create(parkingLotDto);
        return createdParkingLot.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    /**
     * This end-point retrieves a parking lot with the provided id.
     *
     * @param id The id of the parking lot to be retrieved.
     * @return ResponseEntity<ParkingLotDto> Returns the parking lot data in the HTTP response body
     * with a status code of 200 if found, otherwise returns a not found status code.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ParkingLotDto> getParkingLotById(@PathVariable Long id) {
        Optional<ParkingLotDto> parkingLotDto = parkingLotService.getById(id);
        if (parkingLotDto.isPresent()) {
            return ResponseEntity.ok(parkingLotDto.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This end-point retrieves all parking lots.
     *
     * @return ResponseEntity<List < ParkingLotDto>> Returns a list of parking lot data in the HTTP
     * response body with a status code of 200 if found, otherwise returns a no content status
     * code.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ParkingLotDto>> getAllParkingLots() {
        List<ParkingLotDto> parkingLotDtos = parkingLotService.getAll();
        if (parkingLotDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(parkingLotDtos);
    }

    /**
     * This end-point updates a parking lot with the provided id and parking lot data.
     *
     * @param id            The id of the parking lot to be updated.
     * @param parkingLotDto The parking lot data to be updated.
     * @return ResponseEntity<ParkingLotDto> Returns the updated parking lot data in the HTTP response
     * body with a status code of 200 if updated successfully, otherwise returns a not found
     * status code.
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParkingLotDto> updateParkingLot(@PathVariable Long id, @Valid @RequestBody ParkingLotDto parkingLotDto) {
        parkingLotDto.setId(id);
        Optional<ParkingLotDto> updatedParkingLot = parkingLotService.update(parkingLotDto);
        return updatedParkingLot.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * This end-point deletes a parking lot with the provided id.
     *
     * @param id The id of the parking lot to be deleted.
     * @return ResponseEntity<Void> Returns a no content status code if deleted successfully,
     * otherwise returns a not found status code.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteParkingLot(@PathVariable Long id) {
        Optional<ParkingLotDto> deletedParkingLot = parkingLotService.deleteById(id);
        if (deletedParkingLot.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/searchTerm")
    public List<ParkingLotDto> searchParkingLot(@RequestParam(defaultValue = "") String searchTerm,
                                                @RequestParam(defaultValue = "") LocalDate startDate,
                                                @RequestParam(defaultValue = "") LocalDate endDate,
                                                @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                                                @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return parkingLotService.getBySearchTerm(searchTerm, startDate, endDate, page, size);
    }

    /**
     * Retrieves all parking lots owned by the currently logged-in user.
     *
     * @return a ResponseEntity containing a Set of ParkingLotDto objects, or a no-content
     * ResponseEntity if the user has no parking lots.
     */
    @GetMapping(value = "/my-parkinglots", produces = "application/json")
    public ResponseEntity<Set<ParkingLotDto>> getOwnParkingLots(@AuthenticationPrincipal UserEntity user) {
        Optional<Set<ParkingLotDto>> parkingLots = parkingLotService.getParkingLotsByUserId(user.getId());
        if (parkingLots.isPresent()) {
            return ResponseEntity.ok(parkingLots.get());
        }
        return ResponseEntity.noContent().build();
    }
}
