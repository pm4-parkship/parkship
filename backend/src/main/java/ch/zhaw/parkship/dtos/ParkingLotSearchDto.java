package ch.zhaw.parkship.dtos;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.ZonedDateTime;

@Getter
@Setter
public class ParkingLotSearchDto implements Serializable {

    private String searchTerm;
    private ZonedDateTime startDate;
    private ZonedDateTime endDate;

    public ParkingLotSearchDto (String searchTerm, ZonedDateTime startDate, ZonedDateTime endDate){

        this.searchTerm = searchTerm;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
