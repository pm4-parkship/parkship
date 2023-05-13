package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.tag.TagEntity;
import ch.zhaw.parkship.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ParkingLotDto implements Serializable {

    private Long id;
    @NotBlank
    private String name;

    private UserDto owner;

    private String description;

    private Set<String> tags;

    private double longitude;

    private double latitude;

    @NotBlank
    private String address;
    @NotBlank
    private String addressNr;

    private Integer floor;
    private String nr;

    private byte[] picture;

    private double price;

    private ParkingLotState state;

    public ParkingLotDto(ParkingLotEntity parkingLotEntity) {
        this.id = parkingLotEntity.getId();
        this.name = parkingLotEntity.getName();
        this.owner = new UserDto(parkingLotEntity.getOwner());
        this.description = parkingLotEntity.getDescription();
        this.tags = parkingLotEntity.getTags().stream().map(TagEntity::getName).collect(Collectors.toSet());
        this.longitude = parkingLotEntity.getLongitude();
        this.latitude = parkingLotEntity.getLatitude();
        this.address = parkingLotEntity.getAddress();
        this.addressNr = parkingLotEntity.getAddressNr();
        this.floor = parkingLotEntity.getFloor();
        this.nr = parkingLotEntity.getNr();
        this.picture = parkingLotEntity.getPicture();
        this.price = parkingLotEntity.getPrice();
        this.state = parkingLotEntity.getState();
    }

    public ParkingLotDto() {
        this.tags = new HashSet<>();
    }

    @Override
    public String toString() {
        return "ParkingLotDto{" + "id=" + id + ",name=" + name + ", owner=" + owner.id() + ", description='" + description
                + '\'' + ", tags=" + tags + ", longitude=" + longitude + ", latitude=" + latitude
                + ", address='" + address + '\'' + ", addressNr='" + addressNr + '\'' + ", floor=" + floor
                + ", nr='" + nr + '\'' + ", price=" + price + ", state='" + state + '\'' + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ParkingLotDto))
            return false;
        ParkingLotDto that = (ParkingLotDto) o;
        return Objects.equals(id, that.id);
    }
}
