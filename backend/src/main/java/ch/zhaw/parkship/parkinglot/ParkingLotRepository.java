package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.offer.OfferEntity;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ParkingLotRepository extends JpaRepository<ParkingLotEntity, Long> {
    @NonNull
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from ParkingLotEntity p where p.id = :id")
    ParkingLotEntity getByIdLocked(@Param("id") Long id);


    List<ParkingLotEntity> findAllByDescriptionContainsIgnoreCase(String description);

    List<ParkingLotEntity> findAllByAddressContainsIgnoreCase(String address);

    List<ParkingLotEntity> findAllByAddressNrContainsIgnoreCase(String address);

    List<ParkingLotEntity> findAllByOwner_NameContainsIgnoreCaseOrOwner_SurnameContainsIgnoreCase(String name, String surname);

    @Query("SELECT p FROM ParkingLotEntity p " +
            "WHERE NOT EXISTS (SELECT r FROM ReservationEntity r " +
            "                  WHERE r.parkingLot = p " +
            "                  AND (r.from >= :fromDate AND r.from < :toDate " +
            "                       OR r.to > :fromDate AND r.to <= :toDate))")
    List<ParkingLotEntity> findAvailableParkingLotsInRange(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);


    @Query("SELECT p FROM ParkingLotEntity p " +
            "WHERE p = :parkingLot AND " +
            "NOT EXISTS (SELECT r FROM ReservationEntity r " +
            "                  WHERE r.parkingLot = p " +
            "                  AND (r.from >= :fromDate AND r.from < :toDate " +
            "                       OR r.to > :fromDate AND r.to <= :toDate))")
    ParkingLotEntity isParkingLotAvailable(
            @Param("parkingLot") ParkingLotEntity parkingLotEntity,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);

    @Query("SELECT p FROM ParkingLotEntity p " +
            "WHERE p = :parkingLot AND " +
            "EXISTS (SELECT o FROM OfferEntity o " +
            "                  WHERE o.parkingLot = p " +
            "                   AND (o.from <= :fromDate AND :toDate <= o.to) " +
            "                   AND (o.monday OR NOT :monday = true) " +
            "                   AND (o.tuesday OR NOT :tuesday = true) " +
            "                   AND (o.wednesday OR NOT :wednesday = true) " +
            "                   AND (o.thursday OR NOT :thursday = true) " +
            "                   AND (o.friday OR NOT :friday = true) " +
            "                   AND (o.saturday OR NOT :saturday = true) " +
            "                   AND (o.sunday OR NOT :sunday = true))")
    ParkingLotEntity isParkingLotOffered(
            @Param("parkingLot") ParkingLotEntity parkingLotEntity,
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("monday") boolean monday,
            @Param("tuesday") boolean tuesday,
            @Param("wednesday") boolean wednesday,
            @Param("thursday") boolean thursday,
            @Param("friday") boolean friday,
            @Param("saturday") boolean saturday,
            @Param("sunday") boolean sunday);


    Set<ParkingLotEntity> findByOwnerId(Long userId);
}
