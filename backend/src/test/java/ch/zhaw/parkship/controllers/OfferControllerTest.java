package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.offer.OfferController;
import ch.zhaw.parkship.offer.OfferDto;
import ch.zhaw.parkship.offer.OfferEntity;
import ch.zhaw.parkship.offer.OfferService;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class OfferControllerTest {
    private OfferController offerController;

    @Mock
    private OfferService offerService;
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @BeforeEach
    public void setup() {
        offerController = new OfferController(offerService, parkingLotRepository);
    }

    private OfferDto createBasicOfferDto(Long id) {
        return new OfferDto(createBasicOfferEntity(id));
    }

    private OfferEntity createBasicOfferEntity(Long id) {
        OfferEntity offerEntity = new OfferEntity();
        offerEntity.setId(id);
        offerEntity.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L));
        offerEntity.setFrom(LocalDate.now());
        offerEntity.setTo(LocalDate.now().plusDays(7));
        offerEntity.setMonday(true);
        offerEntity.setTuesday(true);
        offerEntity.setWednesday(true);
        offerEntity.setThursday(true);
        offerEntity.setFriday(true);
        offerEntity.setSaturday(true);
        offerEntity.setSunday(true);
        return offerEntity;
    }

    @Test
    void getOfferByIdTest() {
        // arrange
        OfferDto offerDto = new OfferDto();
        offerDto.setId(1L);
        when(offerService.getById(1L)).thenReturn(Optional.of(offerDto));

        // act
        ResponseEntity<OfferDto> result = offerController.getOfferById(1L);

        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().getId());
        verify(offerService, times(1)).getById(1L);
    }

    @Test
    void getAllOffersTest() {
        // arrange
        OfferDto offerDto = new OfferDto();
        offerDto.setId(1L);
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offerDto);

        when(offerService.getAll()).thenReturn(offerDtos);

        // act
        ResponseEntity<List<OfferDto>> result = offerController.getAllOffers();


        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().get(0).getId());
        verify(offerService, times(1)).getAll();
    }

    @Test
    void getOffersByParkingLotTest() {
        // arrange
        OfferDto offer1 = createBasicOfferDto(1L);
        OfferDto offer2 = createBasicOfferDto(2L);
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer1);
        offerDtos.add(offer2);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);

        when(offerService.getByParkingLotId(1L)).thenReturn(offerDtos);

        // act
        ResponseEntity<List<OfferDto>> result = offerController.getOffersByParkingLotId(lot.getId());

        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().get(0).getId());
        Assertions.assertEquals(2L, result.getBody().get(1).getId());
    }

    @Test
    void createOfferTest() {
        //arrange
        OfferEntity offerEntity = createBasicOfferEntity(1L);
        OfferDto offer = new OfferDto(offerEntity);

        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);
        lot.setOfferEntitySet(new HashSet<OfferEntity>());

        when(parkingLotRepository.getByIdLocked(offer.getParkingLotId())).thenReturn(lot);
        when(offerService.create(lot, offerDtos.get(0))).thenReturn(offerEntity);

        // act
        ResponseEntity<List<OfferDto>> result = offerController.createOffer(offerDtos);

        // assert
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().get(0).getId());
    }

    @Test
    void createOfferDatesInPastTest() {
        //arrange

        OfferDto offer = createBasicOfferDto(1L);
        offer.setFrom(LocalDate.now().minusDays(100));
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);
        lot.setOfferEntitySet(new HashSet<OfferEntity>());
        when(parkingLotRepository.getByIdLocked(offer.getParkingLotId())).thenReturn(lot);
        var offerEntity = new OfferEntity();
        offerEntity.setParkingLot(lot);
        when(offerService.create(any(), any())).thenReturn(offerEntity);
        // act
        try {
            ResponseEntity<List<OfferDto>> result = offerController.createOffer(offerDtos);
        } // assert
        catch (ResponseStatusException act) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, act.getStatusCode());
        }
    }

    @Test
    void createOfferDatesAlreadyExistsTest() {
        //arrange
        OfferEntity offerEntity = createBasicOfferEntity(1L);
        OfferDto offer = new OfferDto(offerEntity);
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);
        Set<OfferEntity> existingOffers = new HashSet<OfferEntity>();
        existingOffers.add(offerEntity);
        lot.setOfferEntitySet(existingOffers);

        when(parkingLotRepository.getByIdLocked(offer.getParkingLotId())).thenReturn(lot);

        // act
        try {
            ResponseEntity<List<OfferDto>> result = offerController.createOffer(offerDtos);
        } // assert
        catch (ResponseStatusException act) {
            Assertions.assertEquals(HttpStatus.CONFLICT, act.getStatusCode());
        }


    }

    @Test
    void createOfferDatesLessThanSevenDays() {
        //arrange

        OfferDto offer = createBasicOfferDto(1L);
        offer.setFrom(LocalDate.now());
        offer.setTo(LocalDate.now().plusDays(5));
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);
        lot.setOfferEntitySet(new HashSet<OfferEntity>());
        when(parkingLotRepository.getByIdLocked(offer.getParkingLotId())).thenReturn(lot);
        var offerEntity = new OfferEntity();
        offerEntity.setParkingLot(lot);
        when(offerService.create(any(), any())).thenReturn(offerEntity);
        // act
        try {
            ResponseEntity<List<OfferDto>> result = offerController.createOffer(offerDtos);
        } // assert
        catch (ResponseStatusException act) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, act.getStatusCode());
        }
    }

    @Test
    void createOfferDatesLessThanOneDayTrue() {
        //arrange

        OfferDto offer = createBasicOfferDto(1L);
        offer.setMonday(false);
        offer.setTuesday(false);
        offer.setWednesday(false);
        offer.setThursday(false);
        offer.setFriday(false);
        offer.setSaturday(false);
        offer.setSunday(false);
        ArrayList<OfferDto> offerDtos = new ArrayList<OfferDto>();
        offerDtos.add(offer);

        ParkingLotEntity lot = ParkingLotGenerator.generate(UserGenerator.generate(1L), 1L);
        lot.setOfferEntitySet(new HashSet<OfferEntity>());
        when(parkingLotRepository.getByIdLocked(offer.getParkingLotId())).thenReturn(lot);
        var offerEntity = new OfferEntity();
        offerEntity.setParkingLot(lot);
        when(offerService.create(any(), any())).thenReturn(offerEntity);
        // act
        try {
            ResponseEntity<List<OfferDto>> result = offerController.createOffer(offerDtos);
        } // assert
        catch (ResponseStatusException act) {
            Assertions.assertEquals(HttpStatus.BAD_REQUEST, act.getStatusCode());
        }
    }


}
