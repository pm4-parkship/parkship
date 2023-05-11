package ch.zhaw.parkship.reservation;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * A data transfer object that represents the reservation history for a user,
 * including current and past reservations.
 */
@Getter
@Setter
@NoArgsConstructor
public class ReservationHistoryDto implements Serializable {
    private List<ReservationDto> current;
    private List<ReservationDto> past;

    public ReservationHistoryDto(List<ReservationEntity> current, List<ReservationEntity> past){
        this.current = current.stream().map(ReservationDto::new).toList();
        this.past = past.stream().map(ReservationDto::new).toList();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((current == null) ? 0 : current.hashCode());
        result = prime * result + ((past == null) ? 0 : past.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "ReservationHistoryDto [current=" + current + ", past=" + past + "]";
    }
}