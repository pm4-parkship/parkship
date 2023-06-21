package ch.zhaw.parkship.controllers;

import ch.zhaw.parkship.offer.OfferRepository;
import ch.zhaw.parkship.offer.OfferService;
import ch.zhaw.parkship.parkinglot.ParkingLotController;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import ch.zhaw.parkship.parkinglot.ParkingLotService;
import ch.zhaw.parkship.parkinglot.dtos.ParkingLotDto;
import ch.zhaw.parkship.reservation.ReservationRepository;
import ch.zhaw.parkship.reservation.ReservationService;
import ch.zhaw.parkship.user.UserRepository;
import ch.zhaw.parkship.user.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ParkingLotController.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ParkingLotControllerAdminTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private BCryptPasswordEncoder passwordEncoder;

    @MockBean
    private ParkingLotRepository parkingLotRepository;

    @MockBean
    private ParkingLotService parkingLotService;

    @MockBean
    private ReservationRepository reservationRepository;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private OfferRepository offerRepository;

    @MockBean
    private UserRepository userRepository;
    @MockBean
    private OfferService offerService;

    @Test
    @WithMockUser(username = "user")
    public void getAllParkingLotsWhenPaginationWrong() throws Exception {
        mockMvc.perform(get("/parking-lot")).andExpect(status().isBadRequest());
    }

    @Test
    public void getAllParkingLotsWhenNotAuthorized() throws Exception {
        mockMvc.perform(get("/parking-lot")
                .param("page", "1")
                .param("size", "2")
        ).andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void getAllParkingLotsWhenAuthorized() throws Exception {
        Page<ParkingLotDto> page = Mockito.mock(Page.class);
        when(parkingLotService.findAllPaginated(1, 2)).thenReturn(page);
        mockMvc.perform(get("/parking-lot")
                .param("page", "1")
                .param("size", "2")
        ).andExpect(status().isOk());
    }
}
