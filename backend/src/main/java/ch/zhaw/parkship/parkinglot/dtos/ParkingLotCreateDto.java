package ch.zhaw.parkship.parkinglot.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private Double price;

    private double latitude;
    private double longitude;

    private Set<String> tags = new HashSet<>();
}
