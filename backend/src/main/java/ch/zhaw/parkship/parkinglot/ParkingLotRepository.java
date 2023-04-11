package ch.zhaw.parkship.parkinglot;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    List<ParkingLotEntity> findAllByDescriptionContainsIgnoreCase(String description);
    List<ParkingLotEntity> findAllByAddressContainsIgnoreCase(String address);
    List<ParkingLotEntity> findAllByAddressNrContainsIgnoreCase(String address);
    List<ParkingLotEntity> findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(String name, String surname);

    @Query("""
        SELECT DISTINCT(p) FROM ParkingLotEntity p 
        WHERE ( 
           LOWER(p.description) LIKE lower(concat('%', :query,'%')) OR
           LOWER(p.address) LIKE lower(concat('%', :query,'%')) OR
           LOWER(p.addressNr) LIKE lower(concat('%', :query,'%')) OR
           LOWER(p.owner.name) LIKE lower(concat('%', :query,'%')) OR
           LOWER(p.owner.surname) LIKE lower(concat('%', :query,'%'))
        ) AND EXISTS (SELECT r FROM ReservationEntity r 
            WHERE r.parkingLot.id = p.id AND r.to >= :startDate AND r.from <= :endDate
        )
      """)
    Page<ParkingLotEntity> search(
            @Param("query") String query,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
    );
}
