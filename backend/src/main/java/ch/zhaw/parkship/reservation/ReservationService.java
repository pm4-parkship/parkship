package ch.zhaw.parkship.reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.user.UserRepository;

/**
 * This class provides services for managing ReservationDto objects.
 *
 * It performs CRUD operations on ReservationEntity objects
 *
 * through ReservationRepository, ParkingLotRepository, and UserRepository.
 */
@Service
public class ReservationService {

  @Autowired
  private ReservationRepository reservationRepository;

  @Autowired
  private ParkingLotRepository parkingLotRepository;

  @Autowired
  private UserRepository userRepository;

  /**
   *
   * This method creates a new reservation with the provided reservation data.
   *
   * @param data The reservation data to be saved.
   * @return Optional<ReservationDto> Returns an Optional object containing the saved reservation
   *         data if created successfully, otherwise returns an empty Optional object.
   */
  public Optional<ReservationDto> create(ReservationDto data) {
    var parkingLot = parkingLotRepository.findById(data.getParkingLot().getId());
    var tenant = userRepository.findById(data.getTenant().getId());
    if (parkingLot.isPresent() && tenant.isPresent()) {
      var reservationEntity = new ReservationEntity();
      BeanUtils.copyProperties(data, reservationEntity);
      reservationEntity.setParkingLot(parkingLot.get());
      reservationEntity.setTenant(tenant.get());
      var savedEntity = reservationRepository.save(reservationEntity);
      return Optional.of(new ReservationDto(savedEntity));
    }
    return Optional.empty();
  }

  /**
   *
   * This method creates a new reservation with the provided parking lot id and reservation data.
   *
   * @param parkingLotId The id of the parking lot where the reservation will be created.
   * @param data The reservation data to be saved.
   * @return Optional<ReservationDto> Returns an Optional object containing the saved reservation
   *         data if created successfully, otherwise returns an empty Optional object.
   */
  public Optional<ReservationDto> create(Long parkingLotId, ReservationDto data) {
    data.getParkingLot().setId(parkingLotId);
    return create(data);
  }

  /**
   *
   * This method retrieves a reservation with the provided id.
   *
   * @param id The id of the reservation to be retrieved.
   * @return Optional<ReservationDto> Returns an Optional object containing the reservation data in
   *         the ReservationDto format if found, otherwise returns an empty Optional object.
   */
  public Optional<ReservationDto> getById(Long id) {
    var reservationEntity = reservationRepository.findById(id);
    if (reservationEntity.isPresent()) {
      return Optional.of(new ReservationDto(reservationEntity.get()));
    }
    return Optional.empty();
  }

  /**
   *
   * This method retrieves all reservations.
   *
   * @return List<ReservationDto> Returns a list of reservation data in the ReservationDto format in
   *         the HTTP response body with a status code of 200 if found, otherwise returns a no
   *         content status code.
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
   *
   * This method updates a reservation with the provided reservation data.
   *
   * @param data The reservation data to be updated.
   * @return Optional<ReservationDto> Returns an Optional object containing the updated reservation
   *         data in the ReservationDto format if updated successfully, otherwise returns an empty
   *         Optional object.
   */
  public Optional<ReservationDto> update(ReservationDto data) {
    var optionalEntity = reservationRepository.findById(data.getId());
    if (optionalEntity.isPresent()) {
      var reservationEntity = optionalEntity.get();
      BeanUtils.copyProperties(data, reservationEntity);
      var updatedEntity = reservationRepository.save(reservationEntity);
      return Optional.of(new ReservationDto(updatedEntity));
    }
    return Optional.empty();
  }

  /**
   * This method deletes a reservation with the provided id.
   *
   * @param id The id of the reservation to be deleted.
   * @return Optional<ReservationDto> Returns an Optional object containing the deleted reservation
   *         data in the ReservationDto format if deleted successfully, otherwise returns an empty
   *         Optional object.
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

  public boolean isFreeInDateRange(Long id, LocalDate startDate, LocalDate endDate){
    return reservationRepository.findAllWithOverlappingDates(id,startDate,endDate).isEmpty();
  }

}
