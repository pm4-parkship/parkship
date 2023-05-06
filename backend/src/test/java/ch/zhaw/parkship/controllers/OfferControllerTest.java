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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Optional;
import java.util.List;

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
    public void setup(){
        offerController = new OfferController(offerService, parkingLotRepository);
    }

    private OfferDto createBasicOfferDto(){
        return new OfferDto(createBasicOfferEntity());
    }

    private OfferEntity createBasicOfferEntity(){
        OfferEntity offerEntity = new OfferEntity();
        offerEntity.setId(1L);
        offerEntity.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate()));
        offerEntity.setFrom(LocalDate.of(2023, 1, 1));
        offerEntity.setTo(LocalDate.of(2023, 12, 31));
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



}
