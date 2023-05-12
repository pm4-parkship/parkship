package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.reservation.*;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.availability.AvailabilityService;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.user.UserRepository;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {
    private ReservationController reservationController;
    @Mock
    private ReservationService reservationService;
    @Mock
    private AvailabilityService availabilityService;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private UserRepository userRepository;

    @Captor
    private ArgumentCaptor<UpdateReservationDto> updateReservationDtoCaptor;

    private final UserEntity userEntity = UserGenerator.generate(1L);

    @BeforeEach
    public void setup() {
        reservationController = new ReservationController(
                reservationService, parkingLotRepository, availabilityService, userRepository);
    }

    private CreateReservationDto createCreateReservationDto() {
        Faker faker = new Faker();
        return new CreateReservationDto(
                1L,
                LocalDate.ofEpochDay(faker.date().future(2, 1, TimeUnit.DAYS).toInstant().getEpochSecond()),
                LocalDate.ofEpochDay(faker.date().future(6, 4, TimeUnit.DAYS).toInstant().getEpochSecond()));
    }

    private ReservationEntity createBasicReservationEntity() {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate()));
        reservationEntity.getParkingLot().setId(1L);
        reservationEntity.setTenant(userEntity);
        reservationEntity.setFrom(LocalDate.of(2023, 4, 14));
        reservationEntity.setTo(LocalDate.of(2023, 4, 15));
        return reservationEntity;
    }


    @Test
    void createReservationTest() {
        // arrange
        ParkshipUserDetails user = createParkshipUserDetails(userEntity);
        CreateReservationDto reservationDto = createCreateReservationDto();
        ParkingLotEntity parkingLot = new ParkingLotEntity();
        parkingLot.setId(1L);

        when(parkingLotRepository.getByIdLocked(1L)).thenReturn(parkingLot);
        when(availabilityService.isParkingLotAvailable(any(), any(), any())).thenReturn(true);
        when(reservationService.create(parkingLot, userEntity, reservationDto))
                .thenReturn(createBasicReservationEntity());
        when(userRepository.getReferenceById(1L)).thenReturn(userEntity);

        // act
        ResponseEntity<ReservationDto> act = reservationController.createReservation(reservationDto, user);

        // assert
        Assertions.assertEquals(HttpStatus.CREATED, act.getStatusCode());
        Assertions.assertNotNull(act.getBody());
        Assertions.assertEquals(reservationDto.parkingLotID(), act.getBody().getParkingLot().getId());
        Assertions.assertEquals(userEntity.getId(), act.getBody().getTenant().id());

        verify(availabilityService, times(1)).isParkingLotAvailable(any(), any(), any());
        verify(parkingLotRepository, times(1)).getByIdLocked(reservationDto.parkingLotID());
        verify(reservationService, times(1)).create(parkingLot, userEntity, reservationDto);

    }

    ParkshipUserDetails createParkshipUserDetails(UserEntity user) {
        return new ParkshipUserDetails(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getSurname(), user.getPassword(), user.getUserRole(), user.getUserState());
    }

    @Test
    void createReservationNotAvailableTest() {
        // arrange
        ParkshipUserDetails user = createParkshipUserDetails(userEntity);
        CreateReservationDto reservationDto = createCreateReservationDto();
        ParkingLotEntity parkingLot = new ParkingLotEntity();
        parkingLot.setId(1L);

        when(parkingLotRepository.getByIdLocked(1L)).thenReturn(parkingLot);
        when(availabilityService.isParkingLotAvailable(any(), any(), any())).thenReturn(false);

        try {

            // act
            reservationController.createReservation(reservationDto, user);
        } catch (ResponseStatusException act) {
            Assertions.assertEquals(HttpStatus.CONFLICT, act.getStatusCode());
            Assertions.assertNotNull(act.getBody());
        }

        // assert
        verify(availabilityService, times(1)).isParkingLotAvailable(any(), any(), any());
        verify(parkingLotRepository, times(1)).getByIdLocked(reservationDto.parkingLotID());
        verify(reservationService, never()).create(parkingLot, userEntity, reservationDto);

    }

    @Test
    void getReservationByIdTest() {
        // arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        when(reservationService.getById(1L)).thenReturn(Optional.of(reservationDto));

        // act
        ResponseEntity<ReservationDto> result = reservationController.getReservationById(1L);

        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().getId());
        verify(reservationService, times(1)).getById(1L);
    }


    @Test
    void getAllReservationsTest() {
        // arrange TODO fix after UserEntityInfo
//        ReservationDto reservationDto1 = new ReservationDto();
//        ReservationDto reservationDto2 = new ReservationDto();
//        reservationDto1.setId(1L);
//        reservationDto2.setId(2L);
//
//        when(reservationService.getAllByUser(userEntity.getId())).thenReturn(Arrays.asList(reservationDto1, reservationDto2));
//
//        // act
//        ResponseEntity<List<ReservationDto>> result = reservationController.getAllReservations(userEntity);
//
//        // assert
//        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
//        Assertions.assertNotNull(result.getBody());
//        Assertions.assertEquals(2, result.getBody().size());
//        Assertions.assertEquals(1L, result.getBody().get(0).getId());
//        Assertions.assertEquals(2L, result.getBody().get(1).getId());
    }

    @Test
    void updateReservationTest() {
        // arrange
        UpdateReservationDto updateReservationDto = new UpdateReservationDto(
                2L,
                123L,
                LocalDate.of(2023, 6, 14),
                LocalDate.of(2023, 6, 15));

        ReservationDto reservationDto = new ReservationDto(createBasicReservationEntity());
        reservationDto.setId(2L);
        reservationDto.setFrom(LocalDate.of(2023, 6, 14));
        reservationDto.setTo(LocalDate.of(2023, 6, 15));

        when(reservationService.update(updateReservationDtoCaptor.capture()))
                .thenReturn(Optional.of(reservationDto));

        // act
        ResponseEntity<ReservationDto> act = reservationController.updateReservation(2L, updateReservationDto);

        // assert
        Assertions.assertEquals(HttpStatus.OK, act.getStatusCode());
        Assertions.assertNotNull(act.getBody());
        Assertions.assertEquals(2L, act.getBody().getId());
        Assertions.assertEquals(LocalDate.of(2023, 6, 14), act.getBody().getFrom());
        Assertions.assertEquals(LocalDate.of(2023, 6, 15), act.getBody().getTo());
        verify(reservationService, times(1)).update(updateReservationDtoCaptor.capture());
    }

    @Test
    void getReservationNotFoundTest() {
        // arrange
        when(reservationService.getById(1L)).thenReturn(Optional.empty());

        // act
        ResponseEntity<ReservationDto> result = reservationController.getReservationById(1L);

        // assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(reservationService, times(1)).getById(1L);
    }

    @Test
    void updateReservationNotFoundTest() throws Exception {
        // arrange
        UpdateReservationDto updateReservationDto = new UpdateReservationDto(
                2L,
                123L,
                LocalDate.of(2023, 6, 14),
                LocalDate.of(2023, 6, 15));

        when(reservationService.update(updateReservationDtoCaptor.capture())).thenReturn(Optional.empty());

        // act
        ResponseEntity<ReservationDto> result = reservationController.updateReservation(1L, updateReservationDto);

        // assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(reservationService, times(1)).update(updateReservationDtoCaptor.capture());
    }

    @Test
    public void getUserReservationsTest(){
        // arrange
        List<ReservationDto> reservationDtos = new ArrayList<>();
        when(reservationService.getByUserId(1L,LocalDate.MIN,LocalDate.MAX)).thenReturn(reservationDtos);
        ParkshipUserDetails user = createParkshipUserDetails(userEntity);
        //act
        reservationController.getUserReservations(user, Optional.of(LocalDate.MIN),Optional.of(LocalDate.MAX));
        //assert
        verify(reservationService, times(1)).getByUserId(1L,LocalDate.MIN,LocalDate.MAX);
    }
}
