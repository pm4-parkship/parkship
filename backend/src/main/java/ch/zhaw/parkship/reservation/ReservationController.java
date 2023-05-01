package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.availability.AvailabilityService;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.exceptions.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.exceptions.ReservationNotFoundException;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
@RequestMapping("/reservations")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class ReservationController {


    private final ReservationService reservationService;
    private final ParkingLotRepository parkingLotRepository;
    private final AvailabilityService availabilityService;

    private final UserRepository userRepository;

    /**
     * This end-point creates a new reservation with the provided reservation data.
     *
     * @param createReservationDto The reservation data to be saved.
     * @return ResponseEntity<ReservationDto> Returns the saved reservation data in the HTTP response
     * body with a status code of 201 if created successfully, otherwise returns a bad request
     * status code.
     */

    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<ReservationDto> createReservation(
            @Valid @RequestBody CreateReservationDto createReservationDto,
            @AuthenticationPrincipal ParkshipUserDetails user) {

        validateCreateReservationRequest(createReservationDto);

        ParkingLotEntity parkingLot = parkingLotRepository.getByIdLocked(createReservationDto.parkingLotID());

        if (!availabilityService.isParkingLotAvailable(parkingLot, createReservationDto.from(), createReservationDto.to())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "ParkingLot is not available");
        }
        UserEntity tenant = userRepository.getReferenceById(user.getId());
        ReservationEntity reservationEntity = reservationService.create(parkingLot, tenant, createReservationDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(new ReservationDto(reservationEntity));
    }

    private void validateCreateReservationRequest(CreateReservationDto reservationDto) {
        if (reservationDto == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given object is null");
        }
        if (reservationDto.parkingLotID() == 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given parkingLot is invalid");
        }

        if (!isDateRangeValid(reservationDto.from(), reservationDto.to())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid date range");
        }

        if (!areDatesInFuture(reservationDto.from(), reservationDto.to())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given Range should be in the future");
        }
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
        return reservationDto.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
  /**
   *
   */
  @GetMapping(value = "/user", produces = "application/json")
  public List<ReservationDto> getUserReservations (
          @AuthenticationPrincipal ParkshipUserDetails user, @RequestParam ("from") Optional<LocalDate> from, @RequestParam ("to") Optional<LocalDate> to) {
      LocalDate fromDate ;
      LocalDate toDate;
      if (to.isPresent() || from.isPresent()) {
          if (to.isPresent() && from.isEmpty()){
              toDate = to.get();
              fromDate = MinDate;
          }
         else if (to.isEmpty()) {
              toDate = MaxDate;
              fromDate = from.get();
         } else {
              fromDate = from.get();
              toDate = to.get();
         }
      } else {
          toDate = MaxDate;
          fromDate = LocalDate.now();
      }
      return reservationService.getByUserId(user.getId(), fromDate, toDate);
  }

    /**
     * This end-point retrieves all reservations.
     *
     * @return ResponseEntity<List < ReservationDto>> Returns a list of reservation data in the HTTP
     * response body with a status code of 200 if found, otherwise returns a no content status
     * code.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<ReservationDto>> getAllReservations(
            @AuthenticationPrincipal ParkshipUserDetails user
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        List<ReservationDto> reservationDtos = reservationService.getAllByUser(user.getId());
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
    public ResponseEntity<ReservationDto> updateReservation(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationDto reservationDto) {
        reservationDto = new UpdateReservationDto(reservationDto.parkingLotID(), id, reservationDto.from(), reservationDto.to());
        Optional<ReservationDto> updatedReservation = reservationService.update(reservationDto);
        return updatedReservation.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Cancels a reservation, if the reservation exists, is not yet canceled and the reservation
     * is before the cancelataion deadline.
     *
     * @param id reservation ID
     * @throws ReservationNotFoundException         if the reservation does not exist
     * @throws ReservationCanNotBeCanceledException if the reservation either is too late or the reservation is already canceled.
     */
    @DeleteMapping(value = "/{id}")
    public ResponseEntity cancelReservation(@PathVariable("id") Long id) throws ReservationNotFoundException, ReservationCanNotBeCanceledException {
        return ResponseEntity.ok(reservationService.cancelReservation(id));
    }


    private boolean areDatesInFuture(LocalDate from, LocalDate to) {
        LocalDate today = LocalDate.now();
        return from.isAfter(today) && to.isAfter(today);
    }

    private boolean isDateRangeValid(LocalDate from, LocalDate to) {
        if (from == null || to == null) {
            return false;
        }
        return from.isBefore(to);
    }


}
