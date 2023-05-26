package ch.zhaw.parkship.parkinglot;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PerimeterSearchDto(
        @JsonProperty(value = "latitude", required = true) double latitude,
        @JsonProperty(value = "longitude", required = true) double longitude,
        @JsonProperty(value = "radiusInKM", required = true) int radiusInKM,
        @JsonProperty("page") int page,
        @JsonProperty("pageSize") int pageSize) {

}
