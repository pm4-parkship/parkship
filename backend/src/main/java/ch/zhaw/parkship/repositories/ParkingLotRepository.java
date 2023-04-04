package ch.zhaw.parkship.repositories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ch.zhaw.parkship.entities.ParkingLotEntity;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    List<ParkingLotEntity> findAllByDescriptionContainsIgnoreCase(String description);
    List<ParkingLotEntity> findAllByAddressContainsIgnoreCase(String address);
    List<ParkingLotEntity> findAllByAddressNrContainsIgnoreCase(String address);
    List<ParkingLotEntity> findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(String name, String surname);
}

