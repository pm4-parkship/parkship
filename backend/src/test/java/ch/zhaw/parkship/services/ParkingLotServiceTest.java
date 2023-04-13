package ch.zhaw.parkship.services;

import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = {ParkshipApplication.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ParkingLotServiceTest {
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParkingLotService parkingLotService;

    // Sample data for testing
    private final UserEntity userEntity = new UserEntity();
    private final ParkingLotEntity parkingLotEntity = new ParkingLotEntity();
    ;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(parkingLotService, "blackList", new HashSet<String>(Arrays.asList("street", "and", "the", "on", "a", "at", "be", "i", "you", "to", "it", "not", "that", "of", "do", "have", "what", "we", "in", "get", "this", "near", "close", "next")));

        userEntity.setId(1L);
        userEntity.setEmail("fritz@mail.com");
        userEntity.setUsername("fritz123");
        userEntity.setPassword("verysecure");

        parkingLotEntity.setId(1L);
        parkingLotEntity.setOwner(userEntity);
        parkingLotEntity.setLongitude(15.5);
        parkingLotEntity.setLatitude(16.22);
        parkingLotEntity.setNr("11A");
        parkingLotEntity.setPrice(15.55);
        parkingLotEntity.setState("State");
        parkingLotEntity.setAddress("Muster Street");
        parkingLotEntity.setAddressNr("44");
        parkingLotEntity.setDescription("next to the entrance");
    }

    private ParkingLotDto createParkingLotDto() {
        ParkingLotDto data = new ParkingLotDto();
        var owner = new UserDto(userEntity);
        data.setOwner(owner);
        data.setId(1L);
        data.setLongitude(15.5);
        data.setLatitude(16.22);
        data.setNr("11A");
        data.setPrice(15.55);
        data.setState("State");
        data.setAddress("Muster Street");
        data.setAddressNr("44");
        data.setDescription("next to the entrance");
        return data;
    }

    @Test
    public void testCreate() {
        var data = createParkingLotDto();
        parkingLotService.create(data);
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

    @Test
    public void testGetBySearchTermDescription() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance", null, null, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findAllByDescriptionContainsIgnoreCase("entrance");

    }

    @Test
    public void testGetBySearchTermOwner() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase("max", "max")).thenReturn(expectedReturnValue);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("max", null, null, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase("max", "max");

    }

    @MockitoSettings(strictness = Strictness.LENIENT)
    @Test
    public void testGetBySearchTermAddress() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findAllByAddressContainsIgnoreCase("muster")).thenReturn(expectedReturnValue);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("Muster Street 44", null, null, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findAllByAddressContainsIgnoreCase("muster");
    }

    @Test
    public void testGetBySearchTermFreeInGivenTimeFrame() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);

        LocalDate startDate = LocalDate.of(2023, 4, 8);
        LocalDate endDate = LocalDate.of(2023, 4, 9);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);
        when(reservationService.isFreeInDateRange(1L, startDate, endDate)).thenReturn(true);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance", startDate, endDate, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());

        // Add assertions for other properties
        verify(reservationService, times(1)).isFreeInDateRange(1L, startDate, endDate);

    }

    @Test
    public void testGetBySearchTermNotFreeInGivenTimeFrame() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);

        LocalDate startDate = LocalDate.of(2023, 4, 8);
        LocalDate endDate = LocalDate.of(2023, 4, 9);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);
        when(reservationService.isFreeInDateRange(1L, startDate, endDate)).thenReturn(false);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance", startDate, endDate, 0, 100);
        assertTrue(actualReturnValue.isEmpty());

        // Add assertions for other properties
        verify(reservationService, times(1)).isFreeInDateRange(1L, startDate, endDate);

    }

    @Test
    public void testGetBySearchTermPageOneEmpty() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance", null, null, 1, 1);
        assertTrue(actualReturnValue.isEmpty());
    }

    @Test
    public void testGetBySearchTermPageOneEntry() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        ParkingLotEntity p1 = new ParkingLotEntity();
        p1.setId(1L);
        p1.setDescription("near the entrance");
        p1.setOwner(userEntity);
        expectedReturnValue.add(p1);

        ParkingLotEntity p2 = new ParkingLotEntity();
        p2.setId(2L);
        p2.setDescription("right next to the entrance");
        p2.setOwner(userEntity);
        expectedReturnValue.add(p2);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotDto> actualReturnValue = parkingLotService.getBySearchTerm("entrance", null, null, 1, 1);

        assertEquals(expectedReturnValue.get(1).getId(), actualReturnValue.get(0).getId());
    }

}
