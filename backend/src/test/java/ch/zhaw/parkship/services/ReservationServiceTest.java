package ch.zhaw.parkship.services;

import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.*;
import ch.zhaw.parkship.reservation.exceptions.ReservationCanNotBeCanceledException;
import ch.zhaw.parkship.reservation.exceptions.ReservationNotFoundException;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import ch.zhaw.parkship.util.generator.ReservationGenerator;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {
    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private UserRepository userRepository;

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

    private CreateReservationDto createCreateReservationDto() {

        return new CreateReservationDto(
                1L,
                LocalDate.of(2023, 4, 14),
                LocalDate.of(2023, 4, 15));
    }

    private UpdateReservationDto createUpdateReservationDto() {

        return new UpdateReservationDto(
                1L,
                1L,
                LocalDate.of(2023, 4, 14),
                LocalDate.of(2023, 4, 15));
    }

    @Test
    public void testCreate() {
        ParkingLotEntity entity = ParkingLotGenerator.generate(UserGenerator.generate());
        UserEntity userEntity = UserGenerator.generate();
        when(reservationRepository.save(any())).thenReturn(ReservationGenerator.generate(entity, userEntity, 1L));

        var data = createCreateReservationDto();

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

        var data = createUpdateReservationDto();

        var result = reservationService.update(data);

        assertEquals(1, result.get().getId());
        verify(reservationRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
    }

    @Test
    public void testCancelReservationBeforeDeadline() {
        //Cancelation before deadline
        LocalDate beforeDeadline = LocalDate.now().plusDays(ReservationService.CANCELLATION_DEADLINE + 3);
        ReservationEntity reservation = new ReservationEntity();
        UserEntity user = new UserEntity();
        reservation.setFrom(beforeDeadline);
        reservation.setId(2L);
        reservation.setTenant(UserGenerator.generate());
        reservation.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate()));
        reservation.setState(ReservationState.ACTIVE);
        reservation.setTenant(user);
        ParkingLotEntity parkingLot = new ParkingLotEntity();
        parkingLot.setOwner(user);
        reservation.setParkingLot(parkingLot);
        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));


        try {
            reservationService.cancelReservation(2L);
        } catch (Exception e) {
            fail(e);
        }
        verify(reservationRepository, times(1)).findById(2L);
        verify(reservationRepository, times(1)).save(reservation);
        assertEquals(ReservationState.CANCELED, reservation.getState(), "Reservation state was not set to canceled.");
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    public void testCancelReservationOnDeadline() {

        //Cancelation before deadline edge case
        LocalDate beforeDeadline = LocalDate.now().plusDays(ReservationService.CANCELLATION_DEADLINE);
        UserEntity user = new UserEntity();
        ReservationEntity reservation = new ReservationEntity();
        reservation.setFrom(beforeDeadline);
        reservation.setId(2L);
        reservation.setTenant(user);
        ParkingLotEntity parkingLot = new ParkingLotEntity();
        parkingLot.setOwner(user);
        reservation.setParkingLot(parkingLot);
        reservation.setState(ReservationState.ACTIVE);
        reservation.setTenant(UserGenerator.generate());
        reservation.setParkingLot(ParkingLotGenerator.generate(UserGenerator.generate()));

        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));
        ReservationDto reservationDto = null;
        try {
            reservationDto = reservationService.cancelReservation(2L);
        } catch (Exception e) {
            fail(e);
        }
        assertEquals(2L, reservationDto.getId(), "Id in reservationDto was wrong");
        assertEquals(ReservationState.CANCELED, reservationDto.getReservationState());
        assertEquals(LocalDate.now(), reservationDto.getCancelDate());
        verify(reservationRepository, times(1)).findById(2L);
        verify(reservationRepository, times(1)).save(reservation);
        assertEquals(ReservationState.CANCELED, reservation.getState(), "Reservation state was not set to canceled.");
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    public void testCancelReservationAfterDeadline() {
        //Cancelation after deadline
        LocalDate afterDeadline = LocalDate.now().plusDays(1);
        ReservationEntity reservation = new ReservationEntity();
        reservation.setFrom(afterDeadline);
        reservation.setId(2L);

        when(reservationRepository.findById(2L)).thenReturn(Optional.of(reservation));

        assertThrows(ReservationCanNotBeCanceledException.class, () -> {
            reservationService.cancelReservation(2L);
        }, "No exception thrown, even if cancelation after deadline");
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    public void testCancelReservationNotFound() {
        //Non-existent reservation
        when(reservationRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ReservationNotFoundException.class, () -> {
            reservationService.cancelReservation(2L);
        }, "No exception thrown, even if the reservation does not exist");
        verify(reservationRepository, times(1)).findById(2L);
    }

    @Test
    public void testGetAllReservationsOfUserOwnedParkingLots() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(userEntity));
        userEntity.setParkingLots(Sets.set(parkingLotEntity));
        Set<ReservationEntity> reservations = new HashSet<>();
        ReservationEntity reservation1 = new ReservationEntity();
        reservation1.setId(1L);
        reservation1.setFrom(LocalDate.of(2023, 04, 10));
        reservation1.setTo(LocalDate.of(2023, 04, 20));
        reservation1.setTenant(userEntity);
        reservation1.setParkingLot(parkingLotEntity);
        reservations.add(reservation1);
        ReservationEntity reservation2 = new ReservationEntity();
        reservation2.setId(2L);
        reservation2.setFrom(LocalDate.of(2023, 5, 8));
        reservation2.setTo(LocalDate.of(2023, 5, 18));
        reservation2.setTenant(userEntity);
        reservation2.setParkingLot(parkingLotEntity);
        reservations.add(reservation2);
        parkingLotEntity.setReservationEntitySet(reservations);

        var result = reservationService.getAllReservationsOfUserOwnedParkingLots(1L);
        assertEquals(1, result.getCurrent().size());
        assertEquals(1, result.getPast().size());
        assertEquals(reservation1.getId(), result.getPast().get(0).getId());
        assertEquals(reservation2.getId(), result.getCurrent().get(0).getId());
        verify(userRepository, times(1)).findById(1L);
    }

    public void getReservationByUserTest() {
        when(reservationRepository.findAllByTenant(1L, LocalDate.now(), LocalDate.MAX)).thenReturn(new ArrayList<>());
        reservationService.getByUserId(1L, LocalDate.now(), LocalDate.MAX);
        verify(reservationRepository, times(1)).findAllByTenant(1L, LocalDate.now(), LocalDate.MAX);
    }
}
