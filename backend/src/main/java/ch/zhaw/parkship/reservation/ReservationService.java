package ch.zhaw.parkship.reservation;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.exceptions.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.exceptions.ReservationNotFoundException;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;

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
     * This method deletes a reservation with the provided id.
     *
     * @param id The id of the reservation to be deleted.
     * @return Optional<ReservationDto> Returns an Optional object containing the deleted reservation
     * data in the ReservationDto format if deleted successfully, otherwise returns an empty
     * Optional object.
     */
    @Transactional
    public Optional<ReservationDto> deleteById(Long id) {
        var optionalEntity = reservationRepository.findById(id);
        if (optionalEntity.isPresent()) {
            var reservationEntity = optionalEntity.get();
            var ret = new ReservationDto(reservationEntity);
            reservationRepository.delete(reservationEntity);
            return Optional.of(ret);
        }
        return Optional.empty();
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
}
