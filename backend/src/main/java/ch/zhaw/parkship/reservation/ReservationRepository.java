package ch.zhaw.parkship.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query(
            "SELECT r FROM ReservationEntity r WHERE r.id = ?1 AND r.to >= ?2 AND r.from <= ?3"
    )
    public List<ReservationEntity> findAllWithOverlappingDates(Long id, LocalDate startDate, LocalDate endDate);
    @Query ("SELECT r FROM ReservationEntity r WHERE r.tenant = ?1 AND r.from > ?2 AND r.to < ?3 ORDER BY r.from")
    List<ReservationEntity> findAllByTenant(Long id, LocalDate from, LocalDate to);

}
