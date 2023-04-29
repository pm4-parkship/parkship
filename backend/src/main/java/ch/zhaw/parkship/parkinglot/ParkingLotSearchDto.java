package ch.zhaw.parkship.parkinglot;

import ch.zhaw.parkship.tag.TagDto;
import ch.zhaw.parkship.user.UserDto;
import ch.zhaw.parkship.user.UserEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
public class ParkingLotSearchDto implements Serializable {

    private Long id;
    private String name;

    @NotNull
    private String owner;

    private String address;

    @NotNull
    private Double price;
    @NotNull
    private LocalDate from;
    @NotNull
    private LocalDate to;

    public ParkingLotSearchDto(ParkingLotEntity parkingLotEntity, UserEntity owner) {
        this.id = parkingLotEntity.getId();
        this.name = parkingLotEntity.getName();
        this.owner = owner.getName() + " " + owner.getSurname();
        this.address = parkingLotEntity.getAddress() + " " + parkingLotEntity.getAddressNr();
        this.price = parkingLotEntity.getPrice();
    }

    @Override
    public String toString() {
        return "ParkingLotDto{" + "id=" + id + ", name= " + name + ", owner=" + owner + ", description='"
                + ", address='" + address + '\'' +
                ", price=" + price + ", state='" + '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ParkingLotSearchDto))
            return false;
        ParkingLotSearchDto that = (ParkingLotSearchDto) o;
        return Objects.equals(id, that.id);
    }
}
