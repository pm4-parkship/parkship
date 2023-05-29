package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.parkinglot.dtos.ParkingLotCreateDto;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotSearchDto;
import ch.zhaw.parkship.parkinglot.dtos.PerimeterSearchDto;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.tag.TagDto;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.tag.TagRepository;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * This class provides services for managing ParkingLotDto objects.
 * <p>
 * It performs CRUD operations on ParkingLotEntity objects through ParkingLotRepository and
 * UserRepository.
 */
@Service
@Transactional
@RequiredArgsConstructor
public class ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;
    private final UserRepository userRepository;
    private final ReservationService reservationService;
    private final UserService userService;

    @Value("${search.blacklist}")
    private Set<String> blackList;
    private final TagRepository tagRepository;

    /**
     * This method creates a new parking lot with the provided parking lot data.
     *
     * @param data The parking lot data to be saved.
     * @return Optional<ParkingLotDto> Returns an Optional object containing the saved parking lot
     * data in the ParkingLotDto format if created successfully, otherwise returns an empty
     * Optional object.
     */
    public Optional<ParkingLotDto> create(ParkingLotCreateDto data, UserEntity owner) {
        var parkingLotEntity = new ParkingLotEntity();
        BeanUtils.copyProperties(data, parkingLotEntity);
        parkingLotEntity.setOwner(owner);
        parkingLotEntity.setTags(checkedTags(data.getTags().stream().map(TagDto::new).collect(Collectors.toSet())));
        parkingLotEntity.setId(null);
        parkingLotEntity.setState(ParkingLotState.PENDING);
        var savedEntity = parkingLotRepository.save(parkingLotEntity);
        return Optional.of(new ParkingLotDto(savedEntity));
    }

    /**
     * This method retrieves a parking lot with the provided id.
     *
     * @param id The id of the parking lot to be retrieved.
     * @return Optional<ParkingLotDto> Returns an Optional object containing the parking lot data in
     * the ParkingLotDto format if found, otherwise returns an empty Optional object.
     */
    public Optional<ParkingLotDto> getById(Long id) {
        var parkingLotEntity = parkingLotRepository.findById(id);
        if (parkingLotEntity.isPresent()) {
            return Optional.of(new ParkingLotDto(parkingLotEntity.get()));
        }
        return Optional.empty();
    }

    /**
     * This method retrieves all parking lots.
     *
     * @return List<ParkingLotDto> Returns a list of parking lot data in the ParkingLotDto format in
     * the HTTP response body with a status code of 200 if found, otherwise returns a no
     * content status code.
     */
    public List<ParkingLotDto> getAll() {
        var parkingLotEntities = parkingLotRepository.findAll();
        List<ParkingLotDto> parkingLotDtos = new ArrayList<>();
        for (ParkingLotEntity entity : parkingLotEntities) {
            parkingLotDtos.add(new ParkingLotDto(entity));
        }
        return parkingLotDtos;
    }

    public Page<ParkingLotDto> findAllPaginated(int pageNumber, int size) {
        // Spring starts page numbers at 0, but that's awkward so we start at 1.
        var page = parkingLotRepository.findAll(PageRequest.of(pageNumber - 1, size));
        return page.map(ParkingLotDto::new);
    }

    /**
     * This method updates a parking lot with the provided parking lot data.
     *
     * @param data The parking lot data to be updated.
     * @return Optional<ParkingLotDto> Returns an Optional object containing the updated parking lot
     * data in the ParkingLotDto format if updated successfully, otherwise returns an empty
     * Optional object.
     */
    public Optional<ParkingLotDto> update(ParkingLotDto data) {
        var optionalEntity = parkingLotRepository.findById(data.getId());
        if (optionalEntity.isPresent()) {
            var parkingLotEntity = optionalEntity.get();
            parkingLotEntity.setTags(checkedTags(data.getTags()));
            BeanUtils.copyProperties(data, parkingLotEntity);
            var updatedEntity = parkingLotRepository.save(parkingLotEntity);
            return Optional.of(new ParkingLotDto(updatedEntity));
        }
        return Optional.empty();
    }

    /**
     * This method deletes a parking lot with the provided id.
     *
     * @param id The id of the parking lot to be deleted.
     * @return Optional<ParkingLotDto> Returns an Optional object containing the deleted parking lot
     * data in the ParkingLotDto format if deleted successfully, otherwise returns an empty
     * Optional object.
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

    /**
     * This method retrieves all parking lots which match a search term and are free in a given time frame.
     *
     * @param searchTerm the string, with the words to search the lots by
     * @param startDate  first day on which the lot needs to be free, null if irrelevant
     * @param endDate    last day on which the lot needs to be free, null if irrelevant
     * @return List<ParkingLotDto> Returns a list of parking lot data in the ParkingLotDto format in
     * the HTTP response body with a status code of 200 if found, otherwise returns a no
     * content status code.
     */
    public List<ParkingLotSearchDto> getBySearchTerm(String searchTerm, List<String> tagList, LocalDate startDate, LocalDate endDate, int page, int size) {
        Set<ParkingLotEntity> parkingLots = getParkingLotsFromDatabase(searchTerm);
        Set<ParkingLotEntity> availableParkingLots = filterParkingLotsByDate(startDate, endDate, parkingLots);
        availableParkingLots = filterByTags(availableParkingLots, tagList);
        List<ParkingLotSearchDto> parkingLotSearchDtos = availableParkingLots.stream().map(
                parkingLotEntity -> {
                    ParkingLotSearchDto parkingLotSearchDto = new ParkingLotSearchDto(parkingLotEntity, parkingLotEntity.getOwner());
                    parkingLotSearchDto.setFrom(startDate);
                    parkingLotSearchDto.setTo(endDate);
                    return parkingLotSearchDto;
                }
        ).toList();

        return getParkingLotSearchDtoPage(page, size, parkingLotSearchDtos);
    }

    private Set<ParkingLotEntity> filterByTags(Set<ParkingLotEntity> availableParkingLots, List<String> tagList) {
        if (tagList == null || tagList.isEmpty()) {
            return availableParkingLots;
        }

        return availableParkingLots.stream()
                .filter(parkingLotEntity -> parkingLotEntity.getTags()
                        .stream().anyMatch(tagEntity -> tagList.contains(tagEntity.getName())))
                .collect(Collectors.toSet());
    }


    private Set<ParkingLotEntity> getParkingLotsFromDatabase(String searchTerm) {
        Set<ParkingLotEntity> parkingLots = new HashSet<>();
        Set<String> searchTerms = new HashSet<String>(Arrays.asList(searchTerm.toLowerCase().replaceAll("\\W", " ").split("\\s+")));
        searchTerms.removeAll(blackList);

        for (String term : searchTerms) {
            parkingLots.addAll(parkingLotRepository.findAllByDescriptionContainsIgnoreCase(term));
            parkingLots.addAll(parkingLotRepository.findAllByAddressContainsIgnoreCase(term));
            parkingLots.addAll(parkingLotRepository.findAllByAddressNrContainsIgnoreCase(term));
            parkingLots.addAll(parkingLotRepository.findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(term, term));
        }
        parkingLots = parkingLots.stream().filter(lot -> lot.getState() == ParkingLotState.ACTIVE).collect(Collectors.toSet());

        return parkingLots;
    }

    private Set<ParkingLotEntity> filterParkingLotsByDate(LocalDate startDate, LocalDate endDate, Set<ParkingLotEntity> parkingLots) {
        if (startDate != null && endDate != null) {
            Boolean[] relevantDays = getRelevantDays(startDate, endDate);
            parkingLots = parkingLots.stream()
                    .filter(lot -> (parkingLotRepository.isParkingLotAvailable(lot, startDate, endDate) != null))
                    .filter(lot -> (parkingLotRepository.isParkingLotOffered(lot, startDate, endDate,
                            relevantDays[0], relevantDays[1], relevantDays[2], relevantDays[3],
                            relevantDays[4], relevantDays[5], relevantDays[6]) != null))
                    .collect(Collectors.toSet());
        }
        return parkingLots;
    }

    private Boolean[] getRelevantDays(LocalDate startDate, LocalDate endDate) {
        Boolean relevantDays[] = {false, false, false, false, false, false, false};
        LocalDate current = LocalDate.of(startDate.getYear(), startDate.getMonth(), startDate.getDayOfMonth());
        while (!current.isEqual(endDate.plusDays(1))) {
            relevantDays[current.getDayOfWeek().getValue() - 1] = true;
            current = current.plusDays(1);
        }
        return relevantDays;
    }


    private List<ParkingLotSearchDto> getParkingLotSearchDtoPage(int page, int size, List<ParkingLotSearchDto> parkingLotDtos) {
        int maxIndex = (page + 1) * size;
        int lowestIndex = maxIndex - size;
        int numOfResults = parkingLotDtos.size();

        if (lowestIndex > numOfResults - 1) {
            return Collections.emptyList();
        }

        if (maxIndex > numOfResults) maxIndex -= size - (numOfResults % size);

        return parkingLotDtos.subList(lowestIndex, maxIndex);
    }

    public Optional<Set<ParkingLotDto>> getParkingLotsByUserId(Long userId) {
        Set<ParkingLotEntity> parkingLots = parkingLotRepository.findByOwnerId(userId);
        if (parkingLots.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(parkingLots.stream().map(ParkingLotDto::new).collect(Collectors.toSet()));
    }


    public void updateState(Long parkingLotId, ParkingLotState newState) {
        ParkingLotEntity entity = parkingLotRepository.getReferenceById(parkingLotId);
        entity.setState(newState);
        parkingLotRepository.save(entity);
    }

    private Set<TagEntity> checkedTags(Set<TagDto> tagDtos) {
        Set<TagEntity> tagEntities = new HashSet<>();
        for (TagDto tagDto : tagDtos) {
            if (tagDto.getId() == null) {
                TagEntity tagEntity = tagRepository.findByNameIgnoreCase(tagDto.getName());
                tagEntities.add(tagEntity);
            } else {
                Optional<TagEntity> optionalTagEntity = tagRepository.findById(tagDto.getId());
                tagEntities.add(optionalTagEntity.get());
            }
        }
        return tagEntities;
    }

    public Page<ParkingLotEntity> perimeterSearch(PerimeterSearchDto perimeterSearchDto) {
        return parkingLotRepository.findParkingLotInGeoRange(
                perimeterSearchDto.latitude(), perimeterSearchDto.longitude(), perimeterSearchDto.radiusInKM(),
                PageRequest.of(perimeterSearchDto.page(),
                        perimeterSearchDto.pageSize() == 0 ? 1 : perimeterSearchDto.pageSize()));
    }

}
