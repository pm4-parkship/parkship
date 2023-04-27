package ch.zhaw.parkship.services;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.*;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ReservationService reservationService;

    // Sample data for testing
    private ReservationEntity reservationEntity;
    private ParkingLotEntity parkingLotEntity;

    UserEntity userEntity = new UserEntity();

    @BeforeEach
    public void setUp() {
        userEntity = new UserEntity();
        parkingLotEntity = new ParkingLotEntity();
        parkingLotEntity.setId(1L);
        parkingLotEntity.setOwner(userEntity);
        userEntity.setId(1L);

        reservationEntity = new ReservationEntity();
        reservationEntity.setId(1L);
        reservationEntity.setTenant(userEntity);
        reservationEntity.setParkingLot(parkingLotEntity);
    }

    private ReservationDto createReservationDto() {
        ReservationDto data = new ReservationDto();
        var parkingLot = new ParkingLotDto();
        var tenant = new UserDto(userEntity);
        parkingLot.setId(1L);
        parkingLot.setOwner(tenant);
        data.setId(1L);
        data.setParkingLot(parkingLot);
        data.setTenant(tenant);
        data.setFrom(LocalDate.of(2023, 6, 10));
        data.setTo(LocalDate.of(2023, 6, 11));
        return data;
    }

    @Test
    public void testCreate() {
        ParkingLotEntity entity = ParkingLotGenerator.generate(UserGenerator.generate());
        UserEntity userEntity = UserGenerator.generate();
        when(reservationRepository.save(any())).thenReturn(ReservationGenerator.generate(entity, userEntity, 1L));

        var data = createReservationDto();

        var result = reservationService.create(entity, userEntity, data);

        assertEquals(1, result.getId());

        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
    }

    @Test
    public void testGetById() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));

        var result = reservationService.getById(1L);

        assertEquals(1, result.get().getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAll() {
        List<ReservationEntity> reservationEntities = new ArrayList<>();
        reservationEntities.add(reservationEntity);

        when(reservationRepository.findAll()).thenReturn(reservationEntities);

        var result = reservationService.getAll();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testUpdate() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservationEntity);

        var data = createReservationDto();

        var result = reservationService.update(data);

        assertEquals(1, result.get().getId());
        verify(reservationRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
    }

    @Test
    public void testDeleteById() {
        // Mock the necessary ReservationRepository behavior
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));

        var result = reservationService.deleteById(1L);

        assertEquals(1, result.get().getId());
        // Add assertions for other properties
        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).delete(reservationEntity);
    }

    @Test
    public void testCancelReservation() {

        //Non-existent reservation
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.cancelReservation(2L);
        }, "No exception thrown, even if the reservation does not exist");
        verify(reservationRepository, times(1)).findById(2L);

        //Cancelation before deadline
        LocalDate beforeDeadline = LocalDate.now().plusDays(5);
        ReservationEntity reservation = new ReservationEntity();
        reservation.setFrom(beforeDeadline);
        reservation.setId(2L);
        reservation.setState(ReservationState.ACTIVE);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        try {
            reservationService.cancelReservation(2L);
        } catch (Exception e) {
            System.out.println("Cancelation failed");
        }
        verify(reservationRepository, times(2)).findById(2L);
        verify(reservationRepository, times(1)).save(reservation);
        assertEquals(ReservationState.CANCELED, reservation.getState(), "Reservation state was not set to canceled.");

        //Cancelation before deadline edge case
        beforeDeadline = LocalDate.now().plusDays(2);
        reservation.setFrom(beforeDeadline);
        reservation.setState(ReservationState.ACTIVE);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        try {
            reservationService.cancelReservation(2L);
        } catch (Exception e) {
            System.out.println("Cancelation failed");
        }
        verify(reservationRepository, times(3)).findById(2L);
        verify(reservationRepository, times(2)).save(reservation);
        assertEquals(ReservationState.CANCELED, reservation.getState(), "Reservation state was not set to canceled.");

        //Cancelation after deadline
        LocalDate afterDeadline = LocalDate.now().plusDays(1);
        reservation = new ReservationEntity();
        reservation.setFrom(afterDeadline);
        reservation.setId(2L);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        assertThrows(ReservationCanNotBeCanceledException.class, () -> {
            reservationService.cancelReservation(2L);
        }, "No exception thrown, even if cancelation after deadline");
        verify(reservationRepository, times(4)).findById(2L);

    }

    @Test
    public void getReservationByUserTest() throws Exception {
        when(reservationRepository.findAllByTenant(userEntity,LocalDate.now(),LocalDate.MAX)).thenReturn(new ArrayList<ReservationEntity>());
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));

        try {
            reservationService.getByUserId(1L,LocalDate.now(),LocalDate.MAX);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

        verify(reservationRepository,times(1)).findAllByTenant(userEntity,LocalDate.now(),LocalDate.MAX);
        verify(userRepository,times(1)).findById(1L);

    }

}
