package ch.zhaw.parkship.services;

import ch.zhaw.parkship.authentication.ApplicationUser;
import ch.zhaw.parkship.authentication.ApplicationUserDto;
import ch.zhaw.parkship.authentication.ApplicationUserRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {
  @Mock
  private ParkingLotRepository parkingLotRepository;

  @Mock
  private ApplicationUserRepository userRepository;

  @InjectMocks
  private ParkingLotService parkingLotService;


  ApplicationUser userEntity = new ApplicationUser();


  // Sample data for testing
  private ParkingLotEntity parkingLotEntity;

  @BeforeEach
  public void setUp() {
    userEntity.setId(1L);
    userEntity.setEmail("fritz@mail.com");
    userEntity.setUsername("fritz123");
    userEntity.setPassword("verysecure");

    parkingLotEntity = new ParkingLotEntity();
    parkingLotEntity.setId(1L);
    parkingLotEntity.setOwner(userEntity);
  }

  private ParkingLotDto createParkingLotDto() {
    ParkingLotDto data = new ParkingLotDto();
    var owner = new ApplicationUserDto(userEntity);
    data.setOwner(owner);
    data.setId(1L);
    data.setLongitude(15.5);
    data.setLatitude(16.22);
    data.setNr("11A");
    data.setPrice(15.55);
    data.setState("State");
    return data;
  }

  @Test
    public void testCreate() {
		when(userRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity.getOwner()));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity);

        var data = createParkingLotDto();

        var result = parkingLotService.create(data);

        assertEquals(1, result.get().getId());

        verify(parkingLotRepository, times(1)).save(any(ParkingLotEntity.class));
    }

  @Test
    public void testGetById() {
        when(parkingLotRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity));

        var result = parkingLotService.getById(1L);

        assertEquals(1, result.get().getId());
        verify(parkingLotRepository, times(1)).findById(1L);
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
        when(parkingLotRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity);

        var data = createParkingLotDto();

        var result = parkingLotService.update(data);

        assertEquals(1, result.get().getId());
        verify(parkingLotRepository, times(1)).findById(1L);
        verify(parkingLotRepository, times(1)).save(any(ParkingLotEntity.class));
	}

  @Test
    public void testDeleteById() {
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity));

        var result = parkingLotService.deleteById(1L);

        assertEquals(1, result.get().getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findById(1L);
        verify(parkingLotRepository, times(1)).deleteById(1L);
    }
}
