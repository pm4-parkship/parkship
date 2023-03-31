package ch.zhaw.parkship.parking.dto;

import java.io.Serializable;
import java.util.Set;

import ch.zhaw.parkship.parking.entity.ParkingLotEntity;
import ch.zhaw.parkship.tags.Tag;
//import ch.zhaw.parkship.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ParkingLotDto implements Serializable {

    private Long id;
    //private User owner;
    private String description;
    private Set<Tag> tags;
    private Double longitude;
    private Double latitude;
    private String address;
    private String addressNr;
    private Integer floor;
    private String nr;
    private Double price;
    private String state;

    public ParkingLotDto(ParkingLotEntity parkingLotEntity) {
        this.id = parkingLotEntity.getId();
        //this.owner = parkingLotEntity.getOwner();
        this.description = parkingLotEntity.getDescription();
        this.tags = parkingLotEntity.getTags();
        this.longitude = parkingLotEntity.getLongitude();
        this.latitude = parkingLotEntity.getLatitude();
        this.address = parkingLotEntity.getAddress();
        this.addressNr = parkingLotEntity.getAddressNr();
        this.floor = parkingLotEntity.getFloor();
        this.nr = parkingLotEntity.getNr();
        this.price = parkingLotEntity.getPrice();
        this.state = parkingLotEntity.getState();
    }
}
