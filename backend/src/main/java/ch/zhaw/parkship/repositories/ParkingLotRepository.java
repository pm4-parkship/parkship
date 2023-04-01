package ch.zhaw.parkship.repositories;
import ch.zhaw.parkship.dtos.ParkingLotDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.zhaw.parkship.entities.ParkingLotEntity;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    @Query(
        value = "SELECT * FROM \"parking_lot\" WHERE \"description\" LIKE %?1%",
        nativeQuery = true
    )
    List<ParkingLotEntity> findParkingLotsByDescription(String searchTerm);
}
