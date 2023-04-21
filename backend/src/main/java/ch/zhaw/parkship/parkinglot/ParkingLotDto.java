package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.tag.TagDto;
import ch.zhaw.parkship.user.UserDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotNull
    private UserDto owner;

    private String description;

    private Set<TagDto> tags;

    @NotNull
    private Double longitude;

    @NotNull
    private Double latitude;

    private String address;

    private String addressNr;

    private Integer floor;

    @NotBlank
    private String nr;

    private byte[] picture;

    @NotNull
    private Double price;

    @NotBlank
    private String state;

    public ParkingLotDto(ParkingLotEntity parkingLotEntity) {
        this.id = parkingLotEntity.getId();
        this.owner = new UserDto(parkingLotEntity.getOwner());
        this.description = parkingLotEntity.getDescription();
        this.tags = parkingLotEntity.getTags().stream().map(TagDto::new).collect(Collectors.toSet());
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
        return "ParkingLotDto{" + "id=" + id + ", owner=" + owner + ", description='" + description
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
