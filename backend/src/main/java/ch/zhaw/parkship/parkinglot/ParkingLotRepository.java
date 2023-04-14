package ch.zhaw.parkship.parkinglot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    List<ParkingLotEntity> findAllByDescriptionContainsIgnoreCase(String description);
//test
    List<ParkingLotEntity> findAllByAddressContainsIgnoreCase(String address);

    List<ParkingLotEntity> findAllByAddressNrContainsIgnoreCase(String address);

    List<ParkingLotEntity> findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(String name, String surname);
}
