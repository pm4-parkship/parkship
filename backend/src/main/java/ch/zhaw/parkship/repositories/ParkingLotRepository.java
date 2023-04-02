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
    @Query(
        value = "SELECT * FROM \"parking_lot\" WHERE LOWER(\"description\") LIKE %?1% OR LOWER(\"address\") LIKE %?1%",
        nativeQuery = true
    )
    List<ParkingLotEntity> filterParkingLotsBySearchTerm(String searchTerm);

    List<ParkingLotEntity> findAllByAddressLikeIgnoreCaseOrDescriptionLikeIgnoreCaseOrAddressNrLikeIgnoreCase(String addressTerm, String descriptionTerm, String addressNrTerm);

}

