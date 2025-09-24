package com.acobrazil.fuelsupply.controllers;

import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyResponseDto;
import com.acobrazil.fuelsupply.services.SupplyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/supplies")
@AllArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<SupplyResponseDto> addSupply(
            @RequestPart("supply") SupplyRequestDto supplyDto,
            @RequestPart(value = "pumpPhoto", required = false) MultipartFile pumpPhoto,
            @RequestPart(value = "odometerPhoto", required = false) MultipartFile odometerPhoto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
    	SupplyResponseDto supply = supplyService.createSupply(
                supplyDto, pumpPhoto, odometerPhoto, attachments
        );
        return ResponseEntity.ok(supply);
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
