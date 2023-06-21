package ch.zhaw.parkship.parkinglot.dtos;

import ch.zhaw.parkship.offer.OfferDto;
import ch.zhaw.parkship.parkinglot.ParkingLotEntity;
import ch.zhaw.parkship.tag.TagDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParkingLotUpdateDto {

    private Long id;
    @NotBlank
    private String name;

    private String description;

    @NotBlank
    private String address;

    @NotBlank
    private String addressNr;

    @NotNull
    @PositiveOrZero
    private Double price;

    private double latitude;
    private double longitude;

    private Set<TagDto> tags;

    private List<OfferDto> offers;

    public ParkingLotUpdateDto(ParkingLotEntity entity) {
        name = entity.getName();
        description = entity.getDescription();
        address = entity.getAddress();
        addressNr = entity.getAddressNr();
        price = entity.getPrice();
        latitude = entity.getLatitude();
        longitude = entity.getLongitude();
    }
}
