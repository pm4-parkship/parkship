package ch.zhaw.parkship.parking.dto;


import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ParkingLotSearchDto implements Serializable {

    private String searchTerm;
    public ParkingLotSearchDto (String searchTerm){
        searchTerm = this.searchTerm;
    }
}
