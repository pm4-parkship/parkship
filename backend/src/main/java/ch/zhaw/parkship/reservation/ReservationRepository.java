package ch.zhaw.parkship.reservation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.awt.print.Pageable;
import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, Long> {
    @Query ("SELECT r FROM ReservationEntity r WHERE r.tenant = ?1 AND r.from > ?2 ORDER BY r.from")
    List<ReservationEntity> findAllByTenant(Long id, ZonedDateTime currentTime, Pageable pageable);

}
