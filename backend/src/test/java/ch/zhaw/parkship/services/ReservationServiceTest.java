package ch.zhaw.parkship.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.reservation.ReservationDto;
import ch.zhaw.parkship.reservation.ReservationEntity;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
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

	@BeforeEach
	public void setUp() {
		var userEntity = new UserEntity();
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
		var tenant = new UserDto();
		tenant.setId(1L);
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
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity.getTenant()));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservationEntity);
        when(parkingLotRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity));

        var data = createReservationDto();

        var result = reservationService.create(data);

        assertEquals(1L, result.get().getId());

        verify(parkingLotRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
    }

	@Test
    public void testGetById() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));

        var result = reservationService.getById(1L);

        assertEquals(1L, result.get().getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

	@Test
	public void testGetAll() {
		List<ReservationEntity> reservationEntities = new ArrayList<>();
		reservationEntities.add(reservationEntity);

		when(reservationRepository.findAll()).thenReturn(reservationEntities);

		var result = reservationService.getAll();

		assertEquals(1, result.size());
		assertEquals(1L, result.get(0).getId());

		verify(reservationRepository, times(1)).findAll();
	}

	@Test
    public void testUpdate() {
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));
        when(reservationRepository.save(any(ReservationEntity.class))).thenReturn(reservationEntity);

        var data = createReservationDto();

        var result = reservationService.update(data);

        assertEquals(1L, result.get().getId());
        verify(reservationRepository, times(1)).findById(anyLong());
        verify(reservationRepository, times(1)).save(any(ReservationEntity.class));
	}

	@Test
    public void testDeleteById() {
        // Mock the necessary ReservationRepository behavior
        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservationEntity));

        var result = reservationService.deleteById(1L);

        assertEquals(1L, result.get().getId());
        // Add assertions for other properties
        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).delete(reservationEntity);
    }
}
