package ch.zhaw.parkshhip.controllers;

import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotController;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import ch.zhaw.parkship.user.UserDto;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class ParkingLotControllerTest {
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


    private ParkingLotDto createBasicParkingLotDto() {
        ParkingLotDto parkingLotDto = new ParkingLotDto();
        parkingLotDto.setId(1L);
        parkingLotDto.setOwner(new UserDto(2L, null, null, null, null, null));
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
    public void getAllParkingLotsTest() throws Exception {
        ParkingLotDto parkingLotDto1 = new ParkingLotDto();
        ParkingLotDto parkingLotDto2 = new ParkingLotDto();
        parkingLotDto1.setId(1L);
        parkingLotDto2.setId(2L);

        when(parkingLotService.getAll()).thenReturn(Arrays.asList(parkingLotDto1, parkingLotDto2));

        mockMvc.perform(get("/parking-lot")).andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1)).andExpect(jsonPath("$.[1].id").value(2));
        verify(parkingLotService, times(1)).getAll();
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
        ParkingLotDto parkingLotDto = createBasicParkingLotDto();
        List<ParkingLotDto> expectedReturnValue = new ArrayList<ParkingLotDto>();
        expectedReturnValue.add(parkingLotDto);
        when(parkingLotService.getBySearchTerm("entrance", null, null, 0, 100)).thenReturn(expectedReturnValue);

        mockMvc.perform(get("/parking-lot/searchTerm")
                        .param("searchTerm", "entrance"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1));

        verify(parkingLotService, times(1)).getBySearchTerm("entrance", null, null, 0, 100);
    }
}
