package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.ReservationDto;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.user.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OfferService {
    private final OfferRepository offerRepository;

    /**
     * This method retrieves an offer with the provided id.
     *
     * @param id The id of the offer to be retrieved.
     * @return Optional<OfferDto> Returns an Optional object containing the offer data in
     * the OfferDto format if found, otherwise returns an empty Optional object.
     */
    public Optional<OfferDto> getById(Long id){
        var offerEntity = offerRepository.findById(id);
        if (offerEntity.isPresent()) {
            return Optional.of(new OfferDto(offerEntity.get()));
        }
        return Optional.empty();
    }

    /**
     * This method retrieves all offer.
     *
     * @return List<OfferDto> Returns a list of offer data in the OfferDto format in
     * the HTTP response body with a status code of 200 if found, otherwise returns a no
     * content status code.
     */
    public List<OfferDto> getAll() {
        var offerEntities = offerRepository.findAll();
        List<OfferDto> offerDtos = new ArrayList<>();
        for (OfferEntity entity : offerEntities) {
            offerDtos.add(new OfferDto(entity));
        }
        return offerDtos;
    }

    public List<OfferDto> getByParkingLotId(Long id) {
        var offerEntities = offerRepository.findAllByParkingLot_Id(id);
        List<OfferDto> offerDtos = new ArrayList<>();
        for (OfferEntity entity : offerEntities) {
            offerDtos.add(new OfferDto(entity));
        }
        return offerDtos;
    }

    /**
     * This method creates a new offer with the provided offer data.
     *
     * @param data The offer data to be saved.
     * @return Optional<OfferDto> Returns an Optional object containing the saved offer
     * data if created successfully, otherwise returns an empty Optional object.
     */
    public OfferEntity create(ParkingLotEntity parkingLotEntity, OfferDto data) {
        var offerEntity = new OfferEntity();
        BeanUtils.copyProperties(data, offerEntity);
        offerEntity.setParkingLot(parkingLotEntity);
        offerEntity.setId(null);
        return offerRepository.save(offerEntity);
    }

    /**
     * This method updates an offer with the provided offer data.
     *
     * @param data The offer data to be updated.
     * @return Optional<OfferDto> Returns an Optional object containing the updated offer
     * data in the OfferDto format if updated successfully, otherwise returns an empty
     * Optional object.
     */
    public Optional<OfferDto> update(OfferDto data) {
        var optionalEntity = offerRepository.findById(data.getId());
        if (optionalEntity.isPresent()) {
            var offerEntity = optionalEntity.get();
            BeanUtils.copyProperties(data, offerEntity);
            var updatedEntity = offerRepository.save(offerEntity);
            return Optional.of(new OfferDto(updatedEntity));
        }
        return Optional.empty();
    }

    /**
     * This method deletes an offer with the provided id.
     *
     * @param id The id of the offer to be deleted.
     * @return Optional<OfferDto> Returns an Optional object containing the deleted offer
     * data in the OfferDto format if deleted successfully, otherwise returns an empty
     * Optional object.
     */
    @Transactional
    public Optional<OfferDto> deleteById(Long id) {
        var optionalEntity = offerRepository.findById(id);
        if (optionalEntity.isPresent()) {
            var offerEntity = optionalEntity.get();
            var ret = new OfferDto(offerEntity);
            offerRepository.delete(offerEntity);
            return Optional.of(ret);
        }
        return Optional.empty();
    }
    
}

