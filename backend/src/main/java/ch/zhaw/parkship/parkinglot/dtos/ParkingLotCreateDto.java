package ch.zhaw.parkship.parkinglot.dtos;

import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotCreateDto {
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String addressNr;

    @NotNull
    @Positive
    private Double price;

    private double latitude;
    private double longitude;

    private Set<String> tags = new HashSet<>();

    public ParkingLotCreateDto(ParkingLotEntity entity) {
        name = entity.getName();
        description = entity.getDescription();
        address = entity.getAddress();
        addressNr = entity.getAddressNr();
        price = entity.getPrice();
        latitude = entity.getLatitude();
        longitude = entity.getLongitude();
    }
}
