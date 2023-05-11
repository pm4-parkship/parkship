package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.exceptions.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.exceptions.ReservationNotFoundException;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * This class provides services for managing ReservationDto objects.
 * <p>
 * It performs CRUD operations on ReservationEntity objects
 * <p>
 * through ReservationRepository, ParkingLotRepository, and UserRepository.
 */
@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public static final int CANCELLATION_DEADLINE = 2;


    /**
     * This method creates a new reservation with the provided reservation data.
     *
     * @param data The reservation data to be saved.
     * @return Optional<ReservationDto> Returns an Optional object containing the saved reservation
     * data if created successfully, otherwise returns an empty Optional object.
     */
    public ReservationEntity create(ParkingLotEntity parkingLotEntity, UserEntity tenant, CreateReservationDto data) {
        var reservationEntity = new ReservationEntity();
        reservationEntity.setTo(data.to());
        reservationEntity.setFrom(data.from());
        reservationEntity.setParkingLot(parkingLotEntity);
        reservationEntity.setTenant(tenant);
        reservationEntity.setState(ReservationState.ACTIVE);

        return reservationRepository.save(reservationEntity);
    }


    /**
     * This method retrieves a reservation with the provided id.
     *
     * @param id The id of the reservation to be retrieved.
     * @return Optional<ReservationDto> Returns an Optional object containing the reservation data in
     * the ReservationDto format if found, otherwise returns an empty Optional object.
     */
    public Optional<ReservationDto> getById(Long id) {
        var reservationEntity = reservationRepository.findById(id);
        if (reservationEntity.isPresent()) {
            return Optional.of(new ReservationDto(reservationEntity.get()));
        }
        return Optional.empty();
    }

    /**
     * This method retrieves all reservations.
     *
     * @return List<ReservationDto> Returns a list of reservation data in the ReservationDto format in
     * the HTTP response body with a status code of 200 if found, otherwise returns a no
     * content status code.
     */
    public List<ReservationDto> getAll() {
        var reservationEntities = reservationRepository.findAll();
        List<ReservationDto> reservationDtos = new ArrayList<>();
        for (ReservationEntity entity : reservationEntities) {
            reservationDtos.add(new ReservationDto(entity));
        }
        return reservationDtos;
    }

    /**
     * This method retrieves reservations by customer id
     *
     * @return List<ReservationDto> Returns a list of reservation data in the ReservationDto format in
     * the HTTP response body with a status code of 200 if found, otherwise returns a no
     * content status code.
     */
    public List<ReservationDto> getByUserId(Long userId, LocalDate from, LocalDate to) {
        List<ReservationDto> reservationDtos;
        var reservationEntities = reservationRepository.findAllByTenant(userId, from, to);
        reservationDtos = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationEntities) {
            reservationDtos.add(new ReservationDto(reservationEntity));
        }
        return reservationDtos;
    }

    /**
     * This method updates a reservation with the provided reservation data.
     *
     * @param data The reservation data to be updated.
     * @return Optional<ReservationDto> Returns an Optional object containing the updated reservation
     * data in the ReservationDto format if updated successfully, otherwise returns an empty
     * Optional object.
     */
    public Optional<ReservationDto> update(UpdateReservationDto data) {
        var optionalEntity = reservationRepository.findById(data.reservationID());
        if (optionalEntity.isPresent()) {
            var reservationEntity = optionalEntity.get();
            reservationEntity.setFrom(data.from());
            reservationEntity.setTo(data.to());
            var updatedEntity = reservationRepository.save(reservationEntity);
            return Optional.of(new ReservationDto(updatedEntity));
        }
        return Optional.empty();
    }

    public boolean isFreeInDateRange(Long id, LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findAllWithOverlappingDates(id, startDate, endDate).isEmpty();
    }

    /**
     * Sets a reservation's state to canceled, if the reservation exists,
     * is not yet canceled and the reservation is before the CANCELATION_DEADLINE.
     *
     * @param id
     * @throws ReservationNotFoundException         if the reservation does not exist
     * @throws ReservationCanNotBeCanceledException if the reservation either is too late or the reservation is already canceled.
     */
    @Transactional
    public ReservationDto cancelReservation(Long id) throws ReservationNotFoundException, ReservationCanNotBeCanceledException {
        LocalDate today = LocalDate.now();
        Optional<ReservationEntity> reservationOptional = reservationRepository.findById(id);

        if (reservationOptional.isEmpty()) {
            throw new ReservationNotFoundException("Reservation not found");
        }

        ReservationEntity reservation = reservationOptional.get();

        if (reservation.getFrom().minusDays(CANCELLATION_DEADLINE).isBefore(today)) {
            throw new ReservationCanNotBeCanceledException("It is too late to cancel this reservation");
        }

        if (reservation.getState().equals(ReservationState.CANCELED)) {
            throw new ReservationCanNotBeCanceledException("This reservation is already canceled");
        }
        reservation.setCancelDate(today);
        reservation.setState(ReservationState.CANCELED);
        reservationRepository.save(reservation);
        return new ReservationDto(reservation);
    }

    /**
     * @param userID current user ID
     * @return list of all reservation made by the user
     */
    public List<ReservationDto> getAllByUser(long userID) {
        return reservationRepository
                .findAllByUser(userID)
                .stream()
                .map(ReservationDto::new)
                .collect(Collectors.toList());
    }
    
    /**
     * Retrieves all reservations of parking lots owned by a user with the given id.
     * Returns a {@link ReservationHistoryDto} object that contains two lists of {@link ReservationDto} objects,
     * one for current reservations and one for past reservations.
     *
     * @param id the id of the user.
     * @return a {@link ReservationHistoryDto} object containing two lists of {@link ReservationDto} objects.
     * @throws EntityNotFoundException if the user with the given id is not found in the system.
     */
    public ReservationHistoryDto getAllReservationsOfUserOwnedParkingLots(Long id) {
      var userEntityO = userRepository.findById(id);
      ReservationHistoryDto dto = new ReservationHistoryDto();
      List<ReservationEntity> reservations = new ArrayList<>();
      List<ReservationDto> current = new ArrayList<>();
      List<ReservationDto> past = new ArrayList<>();

      if(userEntityO.isPresent()){
          var userEntity = userEntityO.get();
          var parkingLots = userEntity.getParkingLots();

          for (ParkingLotEntity parkingLotEntity : parkingLots) {
              reservations.addAll(parkingLotEntity.getReservationEntitySet());
          }

          // get the current date
          LocalDate currentDate = LocalDate.now();

          // filter reservations based on the to date
          reservations.stream().map(ReservationDto::new).forEach(reservation -> {
            if (reservation.getTo().isAfter(currentDate) || reservation.getTo().isEqual(currentDate)) {
              current.add(reservation);
            } else {
              past.add( reservation);
            }
          });
      }

      dto.setCurrent(current);
      dto.setPast(past);

      return dto;
  }
}
