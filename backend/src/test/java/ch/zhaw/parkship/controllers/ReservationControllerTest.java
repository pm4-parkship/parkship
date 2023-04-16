package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.availability.AvailabilityService;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.ReservationController;
import ch.zhaw.parkship.reservation.ReservationDto;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserRepository;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private ArgumentCaptor<ReservationDto> reservationDtoCaptor;

    @BeforeEach
    public void setup() {
        reservationController = new ReservationController(
                reservationService, userRepository, parkingLotRepository, availabilityService);
    }

    private ReservationDto createBasicReservationDto() {
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);
        reservationDto.setParkingLot(new ParkingLotDto());
        reservationDto.getParkingLot().setId(1L);
        reservationDto.setTenant(new UserDto(1L, null, null, null, null, null));
        reservationDto.setFrom(LocalDate.of(2023, 4, 14));
        reservationDto.setTo(LocalDate.of(2023, 4, 15));
        return reservationDto;
    }

    private ReservationEntity createBasicReservationEntity() {
        ReservationEntity reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate()));
        reservationEntity.getParkingLot().setId(1L);
        reservationEntity.setTenant(UserGenerator.generate());
        reservationEntity.setFrom(LocalDate.of(2023, 4, 14));
        reservationEntity.setTo(LocalDate.of(2023, 4, 15));
        return reservationEntity;
    }


    @Test
    void createReservationTest() {
        // arrange
        ReservationDto reservationDto = createBasicReservationDto();
        when(availabilityService.isParkingLotAvailable(any(), any(), any())).thenReturn(true);
        when(reservationService.create(any(), any(), reservationDtoCaptor.capture()))
                .thenReturn(createBasicReservationEntity());

        // act
        ResponseEntity<ReservationDto> result = reservationController.createReservation(reservationDto);

        // assert
        Assertions.assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(reservationDto.getParkingLot().getId(), result.getBody().getParkingLot().getId());

        verify(availabilityService, times(1)).isParkingLotAvailable(any(), any(), any());
        verify(parkingLotRepository, times(1)).getByIdLocked(reservationDto.getParkingLot().getId());

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
        // arrange
        ReservationDto reservationDto1 = new ReservationDto();
        ReservationDto reservationDto2 = new ReservationDto();
        reservationDto1.setId(1L);
        reservationDto2.setId(2L);

        when(reservationService.getAll()).thenReturn(Arrays.asList(reservationDto1, reservationDto2));

        // act
        ResponseEntity<List<ReservationDto>> result = reservationController.getAllReservations();

        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(2, result.getBody().size());
        Assertions.assertEquals(1L, result.getBody().get(0).getId());
        Assertions.assertEquals(2L, result.getBody().get(1).getId());
        verify(reservationService, times(1)).getAll();
    }

    @Test
    void updateReservationTest() {
        // arrange
        ReservationDto reservationDto = createBasicReservationDto();
        reservationDto.setId(1L);

        when(reservationService.update(reservationDtoCaptor.capture()))
                .thenReturn(Optional.of(reservationDto));

        // act
        ResponseEntity<ReservationDto> result = reservationController.updateReservation(1L, reservationDto);

        // assert
        Assertions.assertEquals(HttpStatus.OK, result.getStatusCode());
        Assertions.assertNotNull(result.getBody());
        Assertions.assertEquals(1L, result.getBody().getId());
        verify(reservationService, times(1)).update(reservationDtoCaptor.capture());
    }

    @Test
    void deleteReservationTest() {
        // arrange
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(1L);

        when(reservationService.deleteById(1L)).thenReturn(Optional.of(reservationDto));

        // act
        reservationController.deleteReservation(1L);

        // assert
        verify(reservationService, times(1)).deleteById(1L);
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
        ReservationDto reservationDto = createBasicReservationDto();
        reservationDto.setId(1L);
        when(reservationService.update(reservationDtoCaptor.capture())).thenReturn(Optional.empty());

        // act
        ResponseEntity<ReservationDto> result = reservationController.updateReservation(1L, reservationDto);

        // assert
        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        verify(reservationService, times(1)).update(reservationDtoCaptor.capture());
    }
    @Test
    public void deleteReservationNotFoundTest() throws Exception {
        when(reservationService.deleteById(1L)).thenReturn(Optional.empty());


        // act
        reservationController.deleteReservation(1L);

        // assert
        verify(reservationService, times(1)).deleteById(1L);
    }
}
