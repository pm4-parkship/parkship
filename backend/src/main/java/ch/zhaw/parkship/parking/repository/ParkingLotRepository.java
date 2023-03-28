package ch.zhaw.parkship.parking.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.parkship.parking.entity.ParkingLotEntity;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

}
