package ch.zhaw.parkshhip.controllers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import ch.zhaw.parkship.ParkshipApplication;
import ch.zhaw.parkship.parkinglot.ParkingLotDto;
import ch.zhaw.parkship.reservation.ReservationController;
import ch.zhaw.parkship.reservation.ReservationDto;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserDto;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
@SpringBootTest(classes = ParkshipApplication.class)
public class ReservationControllerTest {
  private MockMvc mockMvc;

  @Mock
  private ReservationService reservationService;

  @InjectMocks
  private ReservationController reservationController;

  private ObjectMapper objectMapper;

  @Captor
  private ArgumentCaptor<ReservationDto> reservationDtoCaptor;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());
  }

  private ReservationDto createBasicReservationDto() {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setId(1L);
    reservationDto.setParkingLot(new ParkingLotDto());
    reservationDto.getParkingLot().setId(1L);
    reservationDto.setTenant(new UserDto());
    reservationDto.getTenant().setId(1L);
    reservationDto.setFrom(LocalDate.of(2023, 4, 14));
    reservationDto.setTo(LocalDate.of(2023, 4, 15));
    return reservationDto;
  }

  @Test
  public void createReservationTest() throws Exception {
    ReservationDto reservationDto = createBasicReservationDto();

    String json = objectMapper.writeValueAsString(reservationDto);

    when(reservationService.create(reservationDtoCaptor.capture()))
        .thenReturn(Optional.of(reservationDto));

    mockMvc.perform(post("/reservation").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated()).andExpect(jsonPath("$.id").value(1));

    verify(reservationService, times(1)).create(reservationDtoCaptor.capture());
  }

  @Test
  public void getReservationByIdTest() throws Exception {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setId(1L);

    when(reservationService.getById(1L)).thenReturn(Optional.of(reservationDto));

    mockMvc.perform(get("/reservation/{id}", 1)).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    verify(reservationService, times(1)).getById(1L);
  }

  @Test
  public void getAllReservationsTest() throws Exception {
    ReservationDto reservationDto1 = new ReservationDto();
    ReservationDto reservationDto2 = new ReservationDto();
    reservationDto1.setId(1L);
    reservationDto2.setId(2L);

    when(reservationService.getAll()).thenReturn(Arrays.asList(reservationDto1, reservationDto2));

    mockMvc.perform(get("/reservation")).andExpect(status().isOk())
        .andExpect(jsonPath("$.[0].id").value(1)).andExpect(jsonPath("$.[1].id").value(2));
    verify(reservationService, times(1)).getAll();
  }

  @Test
  public void updateReservationTest() throws Exception {
    ReservationDto reservationDto = createBasicReservationDto();
    reservationDto.setId(1L);

    when(reservationService.update(reservationDtoCaptor.capture()))
        .thenReturn(Optional.of(reservationDto));

    mockMvc
        .perform(put("/reservation/{id}", 1).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationDto)))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1));

    verify(reservationService, times(1)).update(reservationDtoCaptor.capture());
  }

  @Test
  public void deleteReservationTest() throws Exception {
    ReservationDto reservationDto = new ReservationDto();
    reservationDto.setId(1L);

    when(reservationService.deleteById(1L)).thenReturn(Optional.of(reservationDto));

    mockMvc.perform(delete("/reservation/{id}", 1)).andExpect(status().isNoContent());
    verify(reservationService, times(1)).deleteById(1L);
  }

  @Test
	public void getReservationNotFoundTest() throws Exception {
	    when(reservationService.getById(1L)).thenReturn(Optional.empty());

	    mockMvc.perform(get("/reservation/{id}", 1))
	            .andExpect(status().isNotFound());

	    verify(reservationService, times(1)).getById(1L);
	}

  @Test
  public void updateReservationNotFoundTest() throws Exception {
    ReservationDto reservationDto = createBasicReservationDto();
    reservationDto.setId(1L);

    when(reservationService.update(reservationDtoCaptor.capture())).thenReturn(Optional.empty());

    mockMvc
        .perform(put("/reservation/{id}", 1).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(reservationDto)))
        .andExpect(status().isNotFound());

    verify(reservationService, times(1)).update(reservationDtoCaptor.capture());
  }

  @Test
	public void deleteReservationNotFoundTest() throws Exception {
	    when(reservationService.deleteById(1L)).thenReturn(Optional.empty());

	    mockMvc.perform(delete("/reservation/{id}", 1))
	            .andExpect(status().isNotFound());

	    verify(reservationService, times(1)).deleteById(1L);
	}
}
