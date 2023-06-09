package ch.zhaw.parkship.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {


    @Query(
            "SELECT r FROM ReservationEntity r WHERE r.id = ?1 AND ((r.from <= ?2 AND r.to >= ?2) OR (r.from <= ?3 AND r.to >= ?3))"
    )
    List<ReservationEntity> findAllWithOverlappingDates(Long id, LocalDate startDate, LocalDate endDate);
    @Query(
            "SELECT r FROM ReservationEntity r WHERE r.tenant.id = ?1 AND r.from>=?2 AND r.to <= ?3 ORDER BY r.from"
    )
    List<ReservationEntity> findAllByTenant(Long userId, LocalDate from, LocalDate to);
    @Query(
            "SELECT r FROM ReservationEntity r WHERE r.tenant.id = ?1"
    )
    List<ReservationEntity> findAllByUser(long userID);
  
}