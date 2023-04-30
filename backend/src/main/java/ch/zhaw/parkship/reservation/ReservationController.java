package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.availability.AvailabilityService;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.exceptions.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.exceptions.ReservationNotFoundException;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * This class is a Rest Controller for managing ReservationDto objects
 * <p>
 * and exposes various API end-points for CRUD operations.
 */
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ReservationController {


    private final ReservationService reservationService;
    private final UserRepository userRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final AvailabilityService availabilityService;

    /**
     * This end-point creates a new reservation with the provided reservation data.
     *
     * @param reservationDto The reservation data to be saved.
     * @return ResponseEntity<ReservationDto> Returns the saved reservation data in the HTTP response
     * body with a status code of 201 if created successfully, otherwise returns a bad request
     * status code.
     */

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReservationDto> createReservation(
            @Valid @RequestBody ReservationDto reservationDto) {
        validateRequest(reservationDto);

        ParkingLotEntity parkingLot = parkingLotRepository.getByIdLocked(reservationDto.getParkingLot().getId());
        UserEntity tenant = userRepository.getReferenceById(reservationDto.getTenant().id());
        if (!availabilityService.isParkingLotAvailable(parkingLot, reservationDto.getFrom(), reservationDto.getTo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ParkingLot has pending Reservation during given time");
        }

        ReservationEntity reservationEntity = reservationService.create(parkingLot, tenant, reservationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationDto(reservationEntity));
    }


    /**
     * This end-point retrieves a reservation with the provided id.
     *
     * @param id The id of the reservation to be retrieved.
     * @return ResponseEntity<ReservationDto> Returns the reservation data in the HTTP response body
     * with a status code of 200 if found, otherwise returns a not found status code.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<ReservationDto> getReservationById(@PathVariable Long id) {
        Optional<ReservationDto> reservationDto = reservationService.getById(id);
        if (reservationDto.isPresent()) {
            return ResponseEntity.ok(reservationDto.get());
        }
        return ResponseEntity.notFound().build();
    }
  /**
   *
   */
  @GetMapping(value = "/user/{userId}", produces = "application/json")
  public List<ReservationDto> getUserReservations (
          @PathVariable Long userId, @RequestParam ("from") Optional<LocalDate> from, @RequestParam ("to") Optional<LocalDate> to) throws Exception {
      LocalDate fromDate ;
      LocalDate toDate;
      if (to.isPresent() || from.isPresent()) {
          toDate = to.orElseGet(LocalDate::now);
          fromDate = from.orElseGet(LocalDate::now);
      } else {
          toDate = LocalDate.MAX;
          fromDate = LocalDate.now();
      }
      return reservationService.getByUserId(userId, fromDate, toDate);
  }

    /**
     * This end-point retrieves all reservations.
     *
     * @return ResponseEntity<List < ReservationDto>> Returns a list of reservation data in the HTTP
     * response body with a status code of 200 if found, otherwise returns a no content status
     * code.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReservationDto>> getAllReservations() {
        List<ReservationDto> reservationDtos = reservationService.getAll();
        if (reservationDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(reservationDtos);
    }

    /**
     * This end-point updates a reservation with the provided id and reservation data.
     *
     * @param id             The id of the reservation to be updated.
     * @param reservationDto The reservation data to be updated.
     * @return ResponseEntity<ReservationDto> Returns the updated reservation data in the HTTP
     * response body with a status code of 200 if updated successfully, otherwise returns a
     * not found status code.
     */
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

    /**
     * This end-point deletes a reservation with the provided id.
     *
     * @param id The id of the reservation to be deleted.
     * @return ResponseEntity<Void> Returns a no content status code if deleted successfully,
     * otherwise returns a not found status code.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id) {
        Optional<ReservationDto> deletedReservation = reservationService.deleteById(id);
        if (deletedReservation.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Cancels a reservation, if the reservation exists, is not yet canceled and the reservation
     * is before the cancelataion deadline.
     *
     * @param id
     * @throws ReservationNotFoundException         if the reservation does not exist
     * @throws ReservationCanNotBeCanceledException if the reservation either is too late or the reservation is already canceled.
     */
    @PostMapping(value = "/{id}/cancel")
    public void cancelReservation(@PathVariable("id") Long id) throws ReservationNotFoundException, ReservationCanNotBeCanceledException {
        reservationService.cancelReservation(id);
    }


    protected void validateRequest(ReservationDto reservationDto) {

        if (reservationDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given object is null");
        }
        if (reservationDto.getParkingLot() == null || reservationDto.getParkingLot().getId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given parkingLot is invalid");
        }

        if (reservationDto.getTenant() == null || reservationDto.getTenant().id() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given tenant is invalid");
        }

        if (!isDateRangeValid(reservationDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid date range");
        }

        if (!areDatesInFuture(reservationDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given Range should be in the future");
        }
    }


    private boolean areDatesInFuture(ReservationDto reservationDto) {
        LocalDate today = LocalDate.now();
        return !today.isBefore(reservationDto.getFrom()) && !today.isBefore(reservationDto.getTo());
    }

    private boolean isDateRangeValid(ReservationDto reservationDto) {
        if (reservationDto.getFrom() == null || reservationDto.getTo() == null) {
            return false;
        }
        return reservationDto.getFrom().isBefore(reservationDto.getTo());
    }


}
