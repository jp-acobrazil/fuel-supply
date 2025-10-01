package com.acobrazil.fuelsupply.models.dtos;

import java.math.BigDecimal;

public record SupplyRequestDto(
        Integer driverId,
        BigDecimal liters,
        BigDecimal pricePerLiter,
        String plate,
        String fuelType,
        Long odometer,
        Long hourmeter,
        String stationCnpj,
        String stationName,
        String obs
) {}