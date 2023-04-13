package ch.zhaw.parkship.reservation;

import java.sql.Array;
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
import jakarta.validation.Valid;

/**
 *
 * This class is a Rest Controller for managing ReservationDto objects
 *
 * and exposes various API end-points for CRUD operations.
 */
@RestController
@RequestMapping("/reservation")
public class ReservationController {

  @Autowired
  private ReservationService reservationService;

  /**
   *
   * This end-point creates a new reservation with the provided reservation data.
   *
   * @param ReservationDto The reservation data to be saved.
   * @return ResponseEntity<ReservationDto> Returns the saved reservation data in the HTTP response
   *         body with a status code of 201 if created successfully, otherwise returns a bad request
   *         status code.
   */
  @PostMapping(consumes = "application/json", produces = "application/json")
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<ReservationDto> createReservation(
      @Valid @RequestBody ReservationDto ReservationDto) {
    Optional<ReservationDto> createdReservation = reservationService.create(ReservationDto);
    return createdReservation.map(value -> ResponseEntity.status(HttpStatus.CREATED).body(value))
        .orElseGet(() -> ResponseEntity.badRequest().build());
  }

  /**
   *
   */
  @GetMapping(value = "/user/{id}", consumes = "application/json", produces = "application/json")
  public ResponseEntity<ReservationDto[]> getUserReservations (
          @PathVariable Long id){
    Optional<ReservationDto> reservationDto = reservationService.getById(id);
    // declares an Array of integers.
    ReservationDto[] arr;

    // allocating memory for 5 integers.
    arr = new ReservationDto[4];
    return ResponseEntity.ok(arr);
  }

  /**
   *
   * This end-point retrieves a reservation with the provided id.
   *
   * @param id The id of the reservation to be retrieved.
   * @return ResponseEntity<ReservationDto> Returns the reservation data in the HTTP response body
   *         with a status code of 200 if found, otherwise returns a not found status code.
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
   * This end-point retrieves all reservations.
   *
   * @return ResponseEntity<List<ReservationDto>> Returns a list of reservation data in the HTTP
   *         response body with a status code of 200 if found, otherwise returns a no content status
   *         code.
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
   *
   * This end-point updates a reservation with the provided id and reservation data.
   *
   * @param id The id of the reservation to be updated.
   * @param reservationDto The reservation data to be updated.
   * @return ResponseEntity<ReservationDto> Returns the updated reservation data in the HTTP
   *         response body with a status code of 200 if updated successfully, otherwise returns a
   *         not found status code.
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
   *
   * This end-point deletes a reservation with the provided id.
   *
   * @param id The id of the reservation to be deleted.
   * @return ResponseEntity<Void> Returns a no content status code if deleted successfully,
   *         otherwise returns a not found status code.
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

}
