package ch.zhaw.parkship.dtos;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;

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
