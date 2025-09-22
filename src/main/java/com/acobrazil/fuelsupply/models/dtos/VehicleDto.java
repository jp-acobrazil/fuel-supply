package com.acobrazil.fuelsupply.models.dtos;

public record VehicleDto(
        Integer vehicleId,
        String plate,
        String carType,
        String description,
        char isOwn
) {
}
