package ch.zhaw.parkship.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import ch.zhaw.parkship.authentication.ApplicationUser;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
class ParkingLotServiceTest {
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParkingLotService parkingLotService;

    // Sample data for testing
    private ParkingLotEntity parkingLotEntity;

    @BeforeEach
    public void setUp() {
        var userEntity = new UserEntity();
        userEntity.setId(1);
        userEntity.setApplicationUser(new ApplicationUser("fritz@mail.com", "fritz123", "verysecure"));

        parkingLotEntity = new ParkingLotEntity();
        parkingLotEntity.setId(1);
        parkingLotEntity.setOwner(userEntity);
    }

    private ParkingLotDto createParkingLotDto() {
        ParkingLotDto data = new ParkingLotDto();
        var owner = new UserDto();
        owner.setId(1);
        data.setOwner(owner);
        data.setId(1);
        data.setLongitude(15.5);
        data.setLatitude(16.22);
        data.setNr("11A");
        data.setPrice(15.55);
        data.setState("State");
        return data;
    }

    @Test
    public void testCreate() {
		when(userRepository.findById(any(Integer.class))).thenReturn(Optional.of(parkingLotEntity.getOwner()));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity);

        var data = createParkingLotDto();

        var result = parkingLotService.create(data);

        assertEquals(1, result.get().getId());

        verify(parkingLotRepository, times(1)).save(any(ParkingLotEntity.class));
    }

    @Test
    public void testGetById() {
        when(parkingLotRepository.findById(anyInt())).thenReturn(Optional.of(parkingLotEntity));

        var result = parkingLotService.getById(1);

        assertEquals(1, result.get().getId());
        verify(parkingLotRepository, times(1)).findById(1);
    }

    @Test
    public void testGetAll() {
        List<ParkingLotEntity> parkingLotEntities = new ArrayList<>();
        parkingLotEntities.add(parkingLotEntity);

        when(parkingLotRepository.findAll()).thenReturn(parkingLotEntities);

        var result = parkingLotService.getAll();

        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(parkingLotRepository, times(1)).findAll();
    }

    @Test
    public void testUpdate() {
        when(parkingLotRepository.findById(anyInt())).thenReturn(Optional.of(parkingLotEntity));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity);

        var data = createParkingLotDto();

        var result = parkingLotService.update(data);

        assertEquals(1, result.get().getId());
        verify(parkingLotRepository, times(1)).findById(1);
        verify(parkingLotRepository, times(1)).save(any(ParkingLotEntity.class));
	}

    @Test
    public void testDeleteById() {
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findById(anyInt())).thenReturn(Optional.of(parkingLotEntity));

        var result = parkingLotService.deleteById(1);

        assertEquals(1, result.get().getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findById(1);
        verify(parkingLotRepository, times(1)).deleteById(1);
    }
}
