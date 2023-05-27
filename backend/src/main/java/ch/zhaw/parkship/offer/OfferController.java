package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/offer")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class OfferController {

    private final OfferService offerService;
    private final ParkingLotRepository parkingLotRepository;

    /**
     * This end-point creates a new offer with the provided offer data.
     *
     * @param offerDto The offer data to be saved.
     * @return ResponseEntity<OfferDto> Returns the saved reservation data in the HTTP response
     * body with a status code of 201 if created successfully, otherwise returns a bad request
     * status code.
     */
    @Transactional
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public ResponseEntity<List<OfferDto>> createOffer(
            @Valid @RequestBody List<OfferDto> offerDtos) {
        for (OfferDto offerDto : offerDtos) {
            validateRequest(offerDto);
            ParkingLotEntity parkingLot = parkingLotRepository.getByIdLocked(offerDto.getParkingLotId());
            Set<OfferEntity> currentOffers = parkingLot.getOfferEntitySet();
            LocalDate startDate = offerDto.getFrom();
            LocalDate endDate = offerDto.getTo();
            for (OfferEntity o : currentOffers) {

                if ((startDate.isBefore(o.getFrom().plusDays(1)) && o.getTo().isBefore(endDate.plusDays(1))) ||
                        (o.getFrom().isBefore(startDate.plusDays(1)) && startDate.isBefore(o.getTo().plusDays(1))) ||
                        (o.getFrom().isBefore(endDate.plusDays(1)) && endDate.isBefore(o.getTo().plusDays(1)))) {
                    throw new ResponseStatusException(HttpStatus.CONFLICT, "ParkingLot already has Offer during given time");
                }
            }
        }

        List<OfferDto> returnOfferDots = new ArrayList<>();
        for (OfferDto offerDto : offerDtos) {
            ParkingLotEntity parkingLot = parkingLotRepository.getByIdLocked(offerDto.getParkingLotId());
            returnOfferDots.add(new OfferDto(offerService.create(parkingLot, offerDto)));
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(returnOfferDots);
    }

    /**
     * This end-point retrieves all offers.
     *
     * @return ResponseEntity<List < OfferDto>> Returns a list of offer data in the HTTP
     * response body with a status code of 200 if found, otherwise returns a no content status
     * code.
     */
    @GetMapping(produces = "application/json")
    public ResponseEntity<List<OfferDto>> getAllOffers() {
        List<OfferDto> offerDtos = offerService.getAll();
        if (offerDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(offerDtos);
    }

    /**
     * This end-point retrieves an offer with the provided id.
     *
     * @param id The id of the offer to be retrieved.
     * @return ResponseEntity<OfferDto> Returns the offer data in the HTTP response body
     * with a status code of 200 if found, otherwise returns a not found status code.
     */
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable Long id) {
        Optional<OfferDto> offerDto = offerService.getById(id);
        if (offerDto.isPresent()) {
            return ResponseEntity.ok(offerDto.get());
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This end-point retrieves all offers for a specific parking lot by the lot id.
     *
     * @return ResponseEntity<List < OfferDto>> Returns a list of offer data in the HTTP
     * response body with a status code of 200 if found, otherwise returns a no content status
     * code.
     */
    @GetMapping(value = "/parking-lot/{id}", produces = "application/json")
    public ResponseEntity<List<OfferDto>> getOffersByParkingLotId(@PathVariable Long id) {
        List<OfferDto> offerDtos = offerService.getByParkingLotId(id);
        if (offerDtos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(offerDtos);
    }


    /**
     * This end-point deletes an offer with the provided id.
     *
     * @param id The id of the offer to be deleted.
     * @return ResponseEntity<Void> Returns a no content status code if deleted successfully,
     * otherwise returns a not found status code.
     */
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteOffer(@PathVariable Long id) {
        Optional<OfferDto> deletedOffer = offerService.deleteById(id);
        if (deletedOffer.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * This end-point updates an offer with the provided id and reservation data.
     *
     * @param id       The id of the offer to be updated.
     * @param offerDto The offer data to be updated.
     * @return ResponseEntity<OfferDto> Returns the updated reservation data in the HTTP
     * response body with a status code of 200 if updated successfully, otherwise returns a
     * not found status code.
     */
    @PutMapping(value = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<OfferDto> updateOffer(@PathVariable Long id,
                                                @Valid @RequestBody OfferDto offerDto) {
        validateRequest(offerDto);
        offerDto.setId(id);
        Optional<OfferDto> updatedOffer = offerService.update(offerDto);
        if (updatedOffer.isPresent()) {
            return ResponseEntity.ok(updatedOffer.get());
        }
        return ResponseEntity.notFound().build();
    }

    protected void validateRequest(OfferDto offerDto) {
        if (offerDto.getParkingLotId() == null || parkingLotRepository.getByIdLocked(offerDto.getParkingLotId()) == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given parkingLot is invalid");
        }
/*
        if (!isDateRangeValid(offerDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not a valid date range");
        }

        if (!areDatesInFuture(offerDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given Range should be in the future");
        }

        if (!isAtLeastOneDayAvailable(offerDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No available day in range given");
        }

        if (!isAtLeastOneWeek(offerDto)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Given range should be at least 7 days");
        }
        */
    }

    private boolean areDatesInFuture(OfferDto offerDto) {
        LocalDate today = LocalDate.now();
        return today.isBefore(offerDto.getFrom().plusDays(1)) && today.isBefore(offerDto.getTo().plusDays(1));
    }

    private boolean isAtLeastOneDayAvailable(OfferDto offerDto) {
        if (offerDto.getMonday()) return true;
        if (offerDto.getTuesday()) return true;
        if (offerDto.getWednesday()) return true;
        if (offerDto.getThursday()) return true;
        if (offerDto.getFriday()) return true;
        if (offerDto.getSaturday()) return true;
        if (offerDto.getSunday()) return true;
        return false;
    }

    private boolean isAtLeastOneWeek(OfferDto offerDto) {
        return offerDto.getFrom().plusDays(5).isBefore(offerDto.getTo());
    }

    private boolean isDateRangeValid(OfferDto offerDto) {
        if (offerDto.getFrom() == null || offerDto.getTo() == null) {
            return false;
        }
        return offerDto.getFrom().isBefore(offerDto.getTo().plusDays(1));
    }


}
