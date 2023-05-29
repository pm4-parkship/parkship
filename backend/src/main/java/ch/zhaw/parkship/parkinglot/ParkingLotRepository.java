package ch.zhaw.parkship.parkinglot;

import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

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
            "WHERE p.state = ch.zhaw.parkship.parkinglot.ParkingLotState.ACTIVE AND " +
            "NOT EXISTS (SELECT r FROM ReservationEntity r " +
            "                  WHERE r.parkingLot = p " +
            "                  AND (r.from >= :fromDate AND r.from < :toDate " +
            "                       OR r.to > :fromDate AND r.to <= :toDate))")
    List<ParkingLotEntity> findAvailableParkingLotsInRange(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate);


    @Query("SELECT p FROM ParkingLotEntity p " +
            "WHERE p = :parkingLot AND p.state = ch.zhaw.parkship.parkinglot.ParkingLotState.ACTIVE AND " +
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

    @Query("SELECT p from ParkingLotEntity p where (6371 * acos(cos(radians(:latitude)) * cos(radians(p.latitude)) * cos(radians(p.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(p.latitude)))) < :distanceWithInKM")
    Page<ParkingLotEntity> findParkingLotInGeoRange(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distanceWithInKM") double distanceWithInKM,
            Pageable pageable);



}
