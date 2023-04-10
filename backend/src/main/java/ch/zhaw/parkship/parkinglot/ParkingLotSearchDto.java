package ch.zhaw.parkship.parkinglot;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
@Getter
@Setter
public class ParkingLotSearchDto implements Serializable {
    private String searchTerm;
    private LocalDate startDate;
    private LocalDate endDate;

    public ParkingLotSearchDto (String searchTerm, LocalDate startDate, LocalDate endDate){

        this.searchTerm = searchTerm;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
