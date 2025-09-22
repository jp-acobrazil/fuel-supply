package com.acobrazil.fuelsupply.controllers;

import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyResponseDto;
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
    public ResponseEntity<SupplyResponseDto> addSupply(@RequestBody SupplyRequestDto supply, HttpServletRequest request) {
        return ResponseEntity.ok(supplyService.createSupply(supply, request));
    }

    @GetMapping
    public ResponseEntity<Iterable<SupplyResponseDto>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.getAllSupplies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponseDto> getSupplyById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.getSupplyById(id));
    }

}
