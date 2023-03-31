package ch.zhaw.parkship.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ch.zhaw.parkship.entities.ParkingLotEntity;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {

}
