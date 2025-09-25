package com.acobrazil.fuelsupply.models.dtos;

import com.acobrazil.fuelsupply.models.enums.SupplyStatus;

public record SupplyValidationDto(
        Integer approverId,
        SupplyStatus status,
        String comment
) {}