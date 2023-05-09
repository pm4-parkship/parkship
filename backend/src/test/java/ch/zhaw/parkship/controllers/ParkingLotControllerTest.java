package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.parkinglot.*;
import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.util.UserGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ParkingLotControllerTest {
    private MockMvc mockMvc;

    @Mock
    private ParkingLotService parkingLotService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ParkingLotController parkingLotController;

    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ParkingLotDto> parkingLotDtoCaptor;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        MockitoAnnotations.initMocks(this);
        objectMapper = new ObjectMapper();
    }


    private ParkingLotEntity createBasicParkingLotEntity() {
        ParkingLotEntity parkingLotEntity = new ParkingLotEntity();
        parkingLotEntity.setId(1L);
        parkingLotEntity.setOwner(UserGenerator.generate(1L));
        parkingLotEntity.setLongitude(15.2);
        parkingLotEntity.setLatitude(11.22);
        parkingLotEntity.setNr("11A");
        parkingLotEntity.setPrice(444.4);
        parkingLotEntity.setState(ParkingLotState.ACTIVE);
        parkingLotEntity.setAddress("Muster Street");
        parkingLotEntity.setAddressNr("44");
        parkingLotEntity.setDescription("next to the entrance");
        return parkingLotEntity;
    }

    private ParkingLotDto createBasicParkingLotDto() {
        return new ParkingLotDto(createBasicParkingLotEntity());
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    public void createParkingLotTest() throws Exception {
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();

        String json = objectMapper.writeValueAsString(parkingLotDto);

        when(parkingLotService.create(parkingLotDtoCaptor.capture()))
                .thenReturn(Optional.of(parkingLotDto));

        mockMvc.perform(post("/parking-lot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));

        verify(parkingLotService, times(1)).create(parkingLotDtoCaptor.capture());
    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    public void createParkingLotInvalidPriceTest() throws Exception {
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();
        parkingLotDto.setPrice(-15D);

        String json = objectMapper.writeValueAsString(parkingLotDto);

        when(parkingLotService.create(parkingLotDtoCaptor.capture()))
                .thenReturn(Optional.of(parkingLotDto));

        mockMvc.perform(post("/parking-lot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

    }

    @MockitoSettings(strictness = Strictness.WARN)
    @Test
    public void createParkingLotInvalidCoordinatesTest() throws Exception {
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();
        parkingLotDto.setLatitude(-91D);

        String json = objectMapper.writeValueAsString(parkingLotDto);

        when(parkingLotService.create(parkingLotDtoCaptor.capture()))
                .thenReturn(Optional.of(parkingLotDto));

        mockMvc.perform(post("/parking-lot").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isBadRequest());

    }

    @Test
    public void getParkingLotByIdTest() throws Exception {
        ParkingLotDto parkingLotDto = new ParkingLotDto();
        parkingLotDto.setId(1L);

        when(parkingLotService.getById(1L)).thenReturn(Optional.of(parkingLotDto));

        mockMvc.perform(get("/parking-lot/{id}", 1)).andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(parkingLotService, times(1)).getById(1L);
    }

    @Test
    public void updateParkingLotTest() throws Exception {
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();
        parkingLotDto.setId(1L);

        when(parkingLotService.update(parkingLotDto))
                .thenReturn(Optional.of(parkingLotDto));

        mockMvc
                .perform(put("/parking-lot/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parkingLotDto)))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));

        verify(parkingLotService, times(1)).update(parkingLotDtoCaptor.capture());
    }

    @Test
    public void deleteParkingLotTest() throws Exception {
        ParkingLotDto parkingLotDto = new ParkingLotDto();
        parkingLotDto.setId(1L);

        when(parkingLotService.deleteById(1L)).thenReturn(Optional.of(parkingLotDto));

        mockMvc.perform(delete("/parking-lot/{id}", 1)).andExpect(status().isNoContent());
        verify(parkingLotService, times(1)).deleteById(1L);
    }

    @Test
    public void getParkingLotNotFoundTest() throws Exception {
        when(parkingLotService.getById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/parking-lot/{id}", 1))
                .andExpect(status().isNotFound());

        verify(parkingLotService, times(1)).getById(1L);
    }

    @Test
    public void updateParkingLotNotFoundTest() throws Exception {
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();
        parkingLotDto.setId(1L);

        when(parkingLotService.update(parkingLotDtoCaptor.capture())).thenReturn(Optional.empty());

        mockMvc
                .perform(put("/parking-lot/{id}", 1).contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(parkingLotDto)))
                .andExpect(status().isNotFound());

        verify(parkingLotService, times(1)).update(parkingLotDtoCaptor.capture());
    }

    @Test
    public void deleteParkingLotNotFoundTest() throws Exception {
        when(parkingLotService.deleteById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/parking-lot/{id}", 1))
                .andExpect(status().isNotFound());

        verify(parkingLotService, times(1)).deleteById(1L);
    }

    @Test
    public void searchParkingLotTest() throws Exception {
        ParkingLotEntity parkingLotEntity = createBasicParkingLotEntity();
        List<ParkingLotSearchDto> expectedReturnValue = new ArrayList<>();
        expectedReturnValue.add(new ParkingLotSearchDto(parkingLotEntity, parkingLotEntity.getOwner()));
        when(parkingLotService.getBySearchTerm("entrance", List.of(), null, null, 0, 100)).thenReturn(expectedReturnValue);

        mockMvc.perform(get("/parking-lot/searchTerm")
                        .param("searchTerm", "entrance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1));

        verify(parkingLotService, times(1)).getBySearchTerm("entrance",List.of(), null, null, 0, 100);
    }



    @Test
    public void getOwnParkingLotsTest() throws Exception {
        ParkshipUserDetails user = createParkshipUserDetails(UserGenerator.generate(1L));

        ParkingLotDto parkingLotDto1 = new ParkingLotDto();
        ParkingLotDto parkingLotDto2 = new ParkingLotDto();
        parkingLotDto1.setId(1L);
        parkingLotDto2.setId(2L);

        Set<ParkingLotDto> parkingLots = new HashSet<>(Arrays.asList(parkingLotDto1, parkingLotDto2));

        when(parkingLotService.getParkingLotsByUserId(eq(1L))).thenReturn(Optional.of(parkingLots));
        var response = parkingLotController.getOwnParkingLots(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parkingLots, response.getBody());
        verify(parkingLotService, times(1)).getParkingLotsByUserId(user.getId());
    }

    @Test
    public void getOwnParkingLotsNoContentTest() throws Exception {
        ParkshipUserDetails user = createParkshipUserDetails(UserGenerator.generate(1L));

        when(parkingLotService.getParkingLotsByUserId(eq(user.getId()))).thenReturn(Optional.empty());
        var response = parkingLotController.getOwnParkingLots(user);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(parkingLotService, times(1)).getParkingLotsByUserId(user.getId());
    }

    ParkshipUserDetails createParkshipUserDetails(UserEntity user) {
        return new ParkshipUserDetails(user.getId(), user.getEmail(), user.getUsername(), user.getName(), user.getSurname(), user.getPassword(), user.getUserRole(), user.getUserState());
    }
}
