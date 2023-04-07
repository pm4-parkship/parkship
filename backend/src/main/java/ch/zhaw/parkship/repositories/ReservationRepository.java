package ch.zhaw.parkship.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ch.zhaw.parkship.entities.ReservationEntity;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long>{
    @Query(
            "SELECT r FROM ReservationEntity r WHERE r.id = ?1 AND r.to >= ?2 AND r.from <= ?3"
    )
    public List<ReservationEntity> findAllWithOverlappingDates(Long id, LocalDate startDate, LocalDate endDate);
}
