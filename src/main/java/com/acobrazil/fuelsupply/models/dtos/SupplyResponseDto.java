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
        Long hourmeter,
        Integer loadNumber,
        String inRoute,
        String stationCnpj,
        String stationName,
        String obs,
        char status,
        Integer approverId,
        String approverName,
        LocalDateTime approvalDate,
        String approvalComment,
        EmployeeDto driver,
        VehicleDto vehicle
) {
}
