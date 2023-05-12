package ch.zhaw.parkship.offer;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<OfferEntity, Long> {
        List<OfferEntity> findAllByParkingLot_Id(Long id);
}
