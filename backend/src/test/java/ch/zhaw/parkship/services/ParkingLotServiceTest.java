package ch.zhaw.parkship.services;

import ch.zhaw.parkship.parkinglot.*;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.tag.TagDto;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.tag.TagRepository;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;
import ch.zhaw.parkship.util.UserGenerator;
import ch.zhaw.parkship.util.generator.ParkingLotGenerator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ParkingLotServiceTest {
    @Mock
    private ParkingLotRepository parkingLotRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private ReservationService reservationService;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Captor
    ArgumentCaptor<ParkingLotEntity> parkingLotEntityArgumentCaptor;

    @InjectMocks
    private ParkingLotService parkingLotService;
    // Sample data for testing
    private UserEntity userEntity = new UserEntity();

    private ParkingLotEntity parkingLotEntity;

    private TagEntity tagEntity;


    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(parkingLotService, "blackList", new HashSet<String>(Arrays.asList("street", "and", "the", "on", "a", "at", "be", "i", "you", "to", "it", "not", "that", "of", "do", "have", "what", "we", "in", "get", "this", "near", "close", "next")));

        userEntity.setId(1L);
        userEntity.setEmail("fritz@mail.com");
        userEntity.setUsername("fritz123");
        userEntity.setPassword("verysecure");

        parkingLotEntity = new ParkingLotEntity();
        parkingLotEntity.setId(1L);
        parkingLotEntity.setOwner(userEntity);
        parkingLotEntity.getOwner().setName("Max");
        parkingLotEntity.getOwner().setSurname("Muster");
        parkingLotEntity.setId(1L);
        parkingLotEntity.setLongitude(15.5);
        parkingLotEntity.setLatitude(16.22);
        parkingLotEntity.setNr("11A");
        parkingLotEntity.setPrice(15.55);
        parkingLotEntity.setState(ParkingLotState.ACTIVE);
        parkingLotEntity.setAddress("Muster Street");
        parkingLotEntity.setAddressNr("44");
        parkingLotEntity.setDescription("next to the entrance");

        tagEntity = new TagEntity();
        tagEntity.setId(1L);
        tagEntity.setName("schöner parkplatz");
    }

    private ParkingLotDto createParkingLotDto() {
        ParkingLotDto data = new ParkingLotDto();
        var owner = new UserDto(userEntity);
        Set<TagDto> tagDtos = new HashSet<>();
        tagDtos.add(new TagDto("schoener Parkplatz", 1L));
        data.setOwner(owner);
        data.setId(1L);
        data.setLongitude(15.5);
        data.setLatitude(16.22);
        data.setNr("11A");
        data.setPrice(15.55);
        data.setState(ParkingLotState.ACTIVE);
        data.setAddress("Muster Street");
        data.setAddressNr("44");
        data.setDescription("next to the entrance");
        data.setTags(tagDtos);
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

        // assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());

        verify(parkingLotRepository, times(1)).findAll();
    }

    @Test
    public void testSearchByTags() {
        // arrange
        List<ParkingLotEntity> parkingLotEntities = new ArrayList<>();
        parkingLotEntities.add(parkingLotEntity)   ;

        TagEntity garage = new TagEntity();
        garage.setName("Garage");

        ParkingLotEntity entityWithTag = ParkingLotGenerator.generate(UserGenerator.generate());
        entityWithTag.getTags().add(garage);
        parkingLotEntities.add(entityWithTag);

        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase(Mockito.any())).thenReturn(parkingLotEntities);

        // act
        var result = parkingLotService.getBySearchTerm("", List.of("Garage"), null, null,
                0, 100);

        // assertEquals(1, result.size());
        assertEquals(1, result.size());
    }

    @Test
    public void testUpdate() {
        when(parkingLotRepository.findById(anyLong())).thenReturn(Optional.of(parkingLotEntity));
        when(parkingLotRepository.save(any(ParkingLotEntity.class))).thenReturn(parkingLotEntity);
        when(tagRepository.findById(1L)).thenReturn(Optional.ofNullable(tagEntity));

        var data = createParkingLotDto();

        var result = parkingLotService.update(data);

        assertEquals(1, result.get().getId());
        verify(tagRepository, times(1)).findById(1L);
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
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<>();
        expectedReturnValue.add(parkingLotEntity);
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance",List.of(), null, null, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());
        // Add assertions for other properties
        verify(parkingLotRepository, times(1)).findAllByDescriptionContainsIgnoreCase("entrance");

    }

    @Test
    public void testGetBySearchTermOwner() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<>();
        expectedReturnValue.add(parkingLotEntity);
        // Mock the necessary ParkingLotRepository behavior
        when(parkingLotRepository.findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase("max", "max")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("max",List.of(), null, null, 0, 100);
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

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("Muster Street 44",List.of(), null, null, 0, 100);
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
        when(parkingLotRepository.isParkingLotAvailable(parkingLotEntity, startDate, endDate)).thenReturn(parkingLotEntity);
        when(parkingLotRepository.isParkingLotOffered(parkingLotEntity, startDate, endDate, false, false, false, false, false, true, true)).thenReturn(parkingLotEntity);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance",List.of(), startDate, endDate, 0, 100);
        assertEquals(expectedReturnValue.get(0).getId(), actualReturnValue.get(0).getId());

    }

    @Test
    public void testGetBySearchTermNotFreeInGivenTimeFrame() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);

        LocalDate startDate = LocalDate.of(2023, 4, 8);
        LocalDate endDate = LocalDate.of(2023, 4, 9);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance",List.of(), startDate, endDate, 0, 100);
        assertTrue(actualReturnValue.isEmpty());

    }

    @Test
    public void testGetBySearchTermPageOneEmpty() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        expectedReturnValue.add(parkingLotEntity);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance",List.of(), null, null, 1, 1);
        assertTrue(actualReturnValue.isEmpty());
    }

    @Test
    public void testGetBySearchTermInactive(){
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        ParkingLotEntity p1 = new ParkingLotEntity();
        p1.setId(1L);
        p1.setState(ParkingLotState.INACTIVE);
        p1.setDescription("near the entrance");
        p1.setOwner(userEntity);
        expectedReturnValue.add(p1);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("near the entrance", null, null, null, 0, 100);
        assertTrue(actualReturnValue.isEmpty());
      }

    @Test
    public void testGetBySearchTermPageOneEntry() {
        List<ParkingLotEntity> expectedReturnValue = new ArrayList<ParkingLotEntity>();
        ParkingLotEntity p1 = new ParkingLotEntity();
        p1.setId(1L);
        p1.setState(ParkingLotState.ACTIVE);
        p1.setDescription("near the entrance");
        p1.setOwner(userEntity);
        expectedReturnValue.add(p1);

        ParkingLotEntity p2 = new ParkingLotEntity();
        p2.setId(2L);
        p2.setState(ParkingLotState.ACTIVE);
        p2.setDescription("right next to the entrance");
        p2.setOwner(userEntity);
        expectedReturnValue.add(p2);

        // Mock the necessary ParkingLotRepository and ReservationRepository behavior
        when(parkingLotRepository.findAllByDescriptionContainsIgnoreCase("entrance")).thenReturn(expectedReturnValue);

        List<ParkingLotSearchDto> actualReturnValue = parkingLotService.getBySearchTerm("entrance",List.of(), null, null, 1, 1);

        assertEquals(expectedReturnValue.get(1).getId(), actualReturnValue.get(0).getId());
    }

    @Test
    public void getAllParkingLotsOfLoggedInUser_hasParkingLots() {
        var parkingLotEntity1 = new ParkingLotEntity();
        parkingLotEntity1.setId(1L);
        parkingLotEntity1.setOwner(userEntity);

        var parkingLotEntity2 = new ParkingLotEntity();
        parkingLotEntity2.setId(2L);
        parkingLotEntity2.setOwner(userEntity);

        Set<ParkingLotEntity> parkingLots = Set.of(parkingLotEntity1, parkingLotEntity2);


        when(parkingLotRepository.findByOwnerId(userEntity.getId())).thenReturn(parkingLots);

        Optional<Set<ParkingLotDto>> result = parkingLotService.getParkingLotsByUserId(userEntity.getId());

        assertTrue(result.isPresent());
        assertEquals(parkingLots.size(), result.get().size());

        result.get().forEach(parkingLotDto -> {
            Optional<ParkingLotEntity> correspondingEntity = parkingLots.stream()
                    .filter(entity -> entity.getId().equals(parkingLotDto.getId())).findFirst();
            assertTrue(correspondingEntity.isPresent());
            assertEquals(correspondingEntity.get().getOwner().getId(), parkingLotDto.getOwner().id());
        });

        verify(parkingLotRepository, times(1)).findByOwnerId(userEntity.getId());
    }


    @Test
    public void getAllParkingLotsOfLoggedInUser_noParkingLots() {
        Set<ParkingLotEntity> emptyParkingLots = Set.of();

        when(parkingLotRepository.findByOwnerId(userEntity.getId())).thenReturn(emptyParkingLots);

        Optional<Set<ParkingLotDto>> result = parkingLotService.getParkingLotsByUserId(userEntity.getId());

        assertTrue(result.isEmpty());

        verify(parkingLotRepository, times(1)).findByOwnerId(userEntity.getId());
    }


    @Test
    void updateStateTest(){
        // arrange
        ParkingLotEntity entity = ParkingLotGenerator.generate(UserGenerator.generate());
        entity.setId(1L);
        entity.setState(ParkingLotState.INACTIVE);
        when(parkingLotRepository.getReferenceById(1L)).thenReturn(entity);

        // act
        parkingLotService.updateState(1L, ParkingLotState.ACTIVE);

        // assert
        verify(parkingLotRepository, times(1)).getReferenceById(1L);
        verify(parkingLotRepository).save(parkingLotEntityArgumentCaptor.capture());
        ParkingLotEntity capture = parkingLotEntityArgumentCaptor.getValue();
        Assertions.assertEquals(ParkingLotState.ACTIVE, capture.getState());
    }

}