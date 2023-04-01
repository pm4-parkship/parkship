package ch.zhaw.parkship.dtos;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class ParkingLotSearchDto implements Serializable {

    private String searchTerm;
    private Date startDate;
    private Date endDate;

    public ParkingLotSearchDto (String searchTerm, Date startDate, Date endDate){

        this.searchTerm = searchTerm;
        this.startDate = startDate;
        this.endDate = endDate;

    }
}
