package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.common.PaginatedResponse;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotCreateDto;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotSearchDto;
import ch.zhaw.parkship.parkinglot.dtos.PerimeterSearchDto;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationHistoryDto;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserEntity;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ParkingLotController {

    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private ReservationService reservationService;

    private final String DEFAULT_PAGE_NUM = "0";
    private final String DEFAULT_PAGE_SIZE = "100";


    @GetMapping(path = "/perimetersearch")
    public ResponseEntity<PaginatedResponse<ParkingLotDto>> perimeterSearch(PerimeterSearchDto perimeterSearchDto) {
        Page<ParkingLotEntity> result = parkingLotService.perimeterSearch(perimeterSearchDto);
        return ResponseEntity.ok(PaginatedResponse.fromPage(result.map(ParkingLotDto::new)));
    }


    /**
     * This end-point creates a new parking lot with the provided parking lot data.
     *
     * @param parkingLotCreateDto The parking lot data to be saved.
     * @return ResponseEntity<ParkingLotDto> Returns the saved parking lot data in the HTTP response
     * body with a status code of 201 if created successfully, otherwise returns a bad request
     * status code.
     */
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ParkingLotDto> createParkingLot(
            @Valid @RequestBody ParkingLotCreateDto parkingLotCreateDto,
            @AuthenticationPrincipal ParkshipUserDetails user) {
        //validateRequest(parkingLotCreateDto);
        var owner = new UserEntity();
        owner.setId(user.getId());
        Optional<ParkingLotDto> createdParkingLot = parkingLotService.create(parkingLotCreateDto, owner);
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
    @Secured("ADMIN")
    public ResponseEntity<PaginatedResponse<ParkingLotDto>> getAllParkingLots(
            @RequestParam("page") int page,
            @RequestParam("size") int size) {
        if (page < 1 || size < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page number and size must be positive integers.");
        }
        Page<ParkingLotDto> parkinglotPage = parkingLotService.findAllPaginated(page, size);
        if (parkinglotPage.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(PaginatedResponse.fromPage(parkinglotPage));
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
        validateRequest(parkingLotDto);

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
    public List<ParkingLotSearchDto> searchParkingLot(
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "") List<String> tagList,
            @RequestParam(defaultValue = "") LocalDate startDate,
            @RequestParam(defaultValue = "") LocalDate endDate,
            @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        return parkingLotService.getBySearchTerm(searchTerm, tagList, startDate, endDate, page, size);


    }

    /**
     * Retrieves all reservations of parking lots owned by the authenticated user.
     * Returns a {@link ReservationHistoryDto} object that contains two lists of {@link ReservationEntity} objects,
     * one for current reservations and one for past reservations.
     *
     * @param user the authenticated user information.
     * @return a {@link ResponseEntity} containing the {@link ReservationHistoryDto} object and HTTP status code 200 (OK).
     * @throws EntityNotFoundException if the authenticated user is not found in the system.
     */
    @GetMapping(value = "/reservations", produces = "application/json")
    public ResponseEntity<ReservationHistoryDto> getReservations(@AuthenticationPrincipal ParkshipUserDetails user) {
        return ResponseEntity.ok(reservationService.getAllReservationsOfUserOwnedParkingLots(user.getId()));
    }

    /**
     * Retrieves all parking lots owned by the currently logged-in user.
     *
     * @return a ResponseEntity containing a Set of ParkingLotDto objects, or a no-content
     * ResponseEntity if the user has no parking lots.
     */
    @GetMapping(value = "/my-parkinglots", produces = "application/json")
    public ResponseEntity<Set<ParkingLotDto>> getOwnParkingLots(@AuthenticationPrincipal ParkshipUserDetails user) {
        Optional<Set<ParkingLotDto>> parkingLots = parkingLotService.getParkingLotsByUserId(user.getId());
        if (parkingLots.isPresent()) {
            return ResponseEntity.ok(parkingLots.get());
        }
        return ResponseEntity.noContent().build();
    }

    @Transactional
    @Secured("ADMIN")
    @PutMapping("/{id}/update-state/{state}")
    public ResponseEntity<Void> updateParkingLotState(@PathVariable("id") Long id, @PathVariable("state") ParkingLotState newState) {
        parkingLotService.updateState(id, newState);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    protected void validateRequest(ParkingLotDto parkingLotDto) {
        if (parkingLotDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given object is null");
        }
        // TODO
        /*
        if (parkingLotDto.getLatitude() < -90 || parkingLotDto.getLatitude() > 90
                || parkingLotDto.getLongitude() < -180 || parkingLotDto.getLongitude() > 180) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given coordinates are invalid");
        }*/

        if (parkingLotDto.getPrice() < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Price may not be smaller than 0");
        }

    }

}
