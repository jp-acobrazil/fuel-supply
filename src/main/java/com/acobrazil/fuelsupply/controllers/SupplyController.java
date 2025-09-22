package com.acobrazil.fuelsupply.controllers;

import com.acobrazil.fuelsupply.models.Supply;
import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.services.SupplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/supplies")
@AllArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping
    public ResponseEntity<Supply> addSupply(@RequestBody SupplyRequestDto supply, HttpServletRequest request) {
        Supply createdSupply = supplyService.createSupply(supply, request);
        return ResponseEntity.ok(createdSupply);
    }

}
