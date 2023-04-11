package ch.zhaw.parkship.parkinglot;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import ch.zhaw.parkship.reservation.ReservationService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ch.zhaw.parkship.user.UserRepository;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * This class provides services for managing ParkingLotDto objects.
 *
 * It performs CRUD operations on ParkingLotEntity objects through ParkingLotRepository and
 * UserRepository.
 */
@Service
@Transactional
public class ParkingLotService {

  @Autowired
  private ParkingLotRepository parkingLotRepository;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private ReservationService reservationService;

  @Value( "${search.blacklist}")
  private Set<String> blackList;

  /**
   *
   * This method creates a new parking lot with the provided parking lot data.
   *
   * @param data The parking lot data to be saved.
   * @return Optional<ParkingLotDto> Returns an Optional object containing the saved parking lot
   *         data in the ParkingLotDto format if created successfully, otherwise returns an empty
   *         Optional object.
   */
  public Optional<ParkingLotDto> create(ParkingLotDto data) {
    var owner = userRepository.findById(data.getOwner().getId());
    if (owner.isPresent()) {
      var parkingLotEntity = new ParkingLotEntity();
      parkingLotEntity.setOwner(owner.get());
      BeanUtils.copyProperties(data, parkingLotEntity);
      parkingLotEntity.setId(null);
      var savedEntity = parkingLotRepository.save(parkingLotEntity);
      return Optional.of(new ParkingLotDto(savedEntity));
    }
    return Optional.empty();
  }

  /**
   *
   * This method retrieves a parking lot with the provided id.
   *
   * @param id The id of the parking lot to be retrieved.
   * @return Optional<ParkingLotDto> Returns an Optional object containing the parking lot data in
   *         the ParkingLotDto format if found, otherwise returns an empty Optional object.
   */
  public Optional<ParkingLotDto> getById(Long id) {
    var parkingLotEntity = parkingLotRepository.findById(id);
    if (parkingLotEntity.isPresent()) {
      return Optional.of(new ParkingLotDto(parkingLotEntity.get()));
    }
    return Optional.empty();
  }

  /**
   *
   * This method retrieves all parking lots.
   *
   * @return List<ParkingLotDto> Returns a list of parking lot data in the ParkingLotDto format in
   *         the HTTP response body with a status code of 200 if found, otherwise returns a no
   *         content status code.
   */
  public List<ParkingLotDto> getAll() {
    var parkingLotEntities = parkingLotRepository.findAll();
    List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
    for (ParkingLotEntity entity : parkingLotEntities) {
      parkingLotDtos.add(new ParkingLotDto(entity));
    }
    return parkingLotDtos;
  }

  /**
   *
   * This method updates a parking lot with the provided parking lot data.
   *
   * @param data The parking lot data to be updated.
   * @return Optional<ParkingLotDto> Returns an Optional object containing the updated parking lot
   *         data in the ParkingLotDto format if updated successfully, otherwise returns an empty
   *         Optional object.
   */
  public Optional<ParkingLotDto> update(ParkingLotDto data) {
    var optionalEntity = parkingLotRepository.findById(data.getId());
    if (optionalEntity.isPresent()) {
      var parkingLotEntity = optionalEntity.get();
      BeanUtils.copyProperties(data, parkingLotEntity);
      var updatedEntity = parkingLotRepository.save(parkingLotEntity);
      return Optional.of(new ParkingLotDto(updatedEntity));
    }
    return Optional.empty();
  }

  /**
   *
   * This method deletes a parking lot with the provided id.
   *
   * @param id The id of the parking lot to be deleted.
   * @return Optional<ParkingLotDto> Returns an Optional object containing the deleted parking lot
   *         data in the ParkingLotDto format if deleted successfully, otherwise returns an empty
   *         Optional object.
   */
  public Optional<ParkingLotDto> deleteById(Long id) {
    var optionalEntity = parkingLotRepository.findById(id);
    if (optionalEntity.isPresent()) {
      var parkingLotEntity = optionalEntity.get();
      var ret = Optional.of(new ParkingLotDto(parkingLotEntity));
      parkingLotRepository.deleteById(id);
      return ret;
    }
    return Optional.empty();
  }


  public List<ParkingLotDto> getBySearchTerm(String searchTerm, LocalDate startDate, LocalDate endDate, int page, int size){
    Set<ParkingLotEntity> parkingLots = new HashSet<>();
    Set <String> searchTerms = new HashSet<String>(Arrays.asList(searchTerm.toLowerCase().replaceAll("\\W"," ").split("\\s+")));
    searchTerms.removeAll(blackList);

    for(String term : searchTerms){
      parkingLots.addAll(parkingLotRepository.findAllByDescriptionContainsIgnoreCase(term));
      parkingLots.addAll(parkingLotRepository.findAllByAddressContainsIgnoreCase(term));
      parkingLots.addAll(parkingLotRepository.findAllByAddressNrContainsIgnoreCase(term));
      parkingLots.addAll(parkingLotRepository.findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(term,term));
    }

    if(startDate != null && endDate != null) {
      parkingLots = parkingLots.stream()
              .filter(lot -> reservationService.isFreeInDateRange(lot.getId(), startDate, endDate))
              .collect(Collectors.toSet());
    }
    
    List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
    for (ParkingLotEntity entity : parkingLots) {
      parkingLotDtos.add(new ParkingLotDto(entity));
    }

    return getParkingLotDtosPage(page, size, parkingLotDtos);
  }


  private List<ParkingLotDto> getParkingLotDtosPage(int page, int size, List<ParkingLotDto> parkingLotDtos) {
    int maxIndex = (page +1)* size;
    int lowestIndex = maxIndex- size;
    int numOfResults = parkingLotDtos.size();

    if(lowestIndex > numOfResults-1) {
     parkingLotDtos.clear();
     return parkingLotDtos;
    }

    if(maxIndex > numOfResults) maxIndex -= size - (numOfResults % size);

    return parkingLotDtos.subList(lowestIndex, maxIndex);
  }
}
