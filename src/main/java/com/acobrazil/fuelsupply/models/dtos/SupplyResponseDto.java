package com.acobrazil.fuelsupply.models.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record SupplyResponseDto(
        Long id,
        LocalDateTime date,
        BigDecimal liters,
        BigDecimal pricePerLiter,
        String fuelType,
        Long odometer,
        String obs,
        char status,
        Integer approverId,
        String approvalComment,
        DriverDto driver,
        VehicleDto vehicle
) {
}
