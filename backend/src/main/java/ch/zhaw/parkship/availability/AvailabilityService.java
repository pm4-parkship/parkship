package ch.zhaw.parkship.availability;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.parkinglot.ParkingLotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final ParkingLotRepository parkingLotRepository;

    @Transactional(propagation = Propagation.MANDATORY)
    public List<ParkingLotEntity> getAllAvailableParkingLots(LocalDate from, LocalDate to) {
        return parkingLotRepository.findAvailableParkingLotsInRange(from, to);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public boolean isParkingLotAvailable(ParkingLotEntity parkingLotEntity, LocalDate from, LocalDate to) {
        return parkingLotRepository.isParkingLotAvailable(parkingLotEntity, from, to) != null;
    }


}
