package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.parkinglot.*;
import ch.zhaw.parkship.user.ParkshipUserDetails;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import ch.zhaw.parkship.util.UserGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @InjectMocks
    private ParkingLotController parkingLotController;

    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<ParkingLotDto> parkingLotDtoCaptor;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        objectMapper = new ObjectMapper();
    }


    private ParkingLotEntity createBasicParkingLotEntity() {
        ParkingLotEntity parkingLotDto = new ParkingLotEntity();
        parkingLotDto.setId(1L);
        parkingLotDto.setOwner(UserGenerator.generate());
        parkingLotDto.setLongitude(15.2);
        parkingLotDto.setLatitude(11.22);
        parkingLotDto.setNr("11A");
        parkingLotDto.setPrice(444.4);
        parkingLotDto.setState("State");
        parkingLotDto.setAddress("Muster Street");
        parkingLotDto.setAddressNr("44");
        parkingLotDto.setDescription("next to the entrance");
        return parkingLotDto;
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

        when(parkingLotService.update(parkingLotDtoCaptor.capture()))
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
        ParkingLotEntity parkingLotDto = createBasicParkingLotEntity();
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
        when(parkingLotService.getBySearchTerm("entrance", null, null, 0, 100)).thenReturn(expectedReturnValue);

        mockMvc.perform(get("/parking-lot/searchTerm")
                        .param("searchTerm", "entrance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1));

        verify(parkingLotService, times(1)).getBySearchTerm("entrance", null, null, 0, 100);
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
