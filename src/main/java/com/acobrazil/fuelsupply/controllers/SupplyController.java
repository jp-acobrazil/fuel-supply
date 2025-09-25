package com.acobrazil.fuelsupply.controllers;

import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyResponseDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyValidationDto;
import com.acobrazil.fuelsupply.services.SupplyService;
import lombok.AllArgsConstructor;
import java.util.List;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/supplies")
@AllArgsConstructor
public class SupplyController {

    private final SupplyService supplyService;

    @PostMapping
    public ResponseEntity<SupplyResponseDto> createSupply(@RequestBody SupplyRequestDto supplyDto) {
    	SupplyResponseDto supply = supplyService.createSupply(supplyDto);
        return ResponseEntity.ok(supply);
    }
    
    @PostMapping(value = "/{supplyId}/files", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadSupplyFiles(
            @PathVariable Long supplyId,
            @RequestPart(value = "pumpPhoto", required = false) MultipartFile pumpPhoto,
            @RequestPart(value = "odometerPhoto", required = false) MultipartFile odometerPhoto,
            @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    ) {
        supplyService.saveSupplyPhotos(supplyId, pumpPhoto, odometerPhoto, attachments);
        return ResponseEntity.ok("Arquivos enviados com sucesso!");
    }

    @GetMapping
    public ResponseEntity<Iterable<SupplyResponseDto>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.getAllSupplies());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplyResponseDto> getSupplyById(@PathVariable Long id) {
        return ResponseEntity.ok(supplyService.getSupplyById(id));
    }
    
    @GetMapping("/driver/{driverId}")
    public ResponseEntity<List<SupplyResponseDto>> getSuppliesByDriverId(@PathVariable Long driverId) {
		List<SupplyResponseDto> supplies = supplyService.getSuppliesByDriverId(driverId);
		return ResponseEntity.ok(supplies);
	}
    
    @PatchMapping("/{id}/status")
    public ResponseEntity<SupplyResponseDto> validateSupply(
            @PathVariable Long id,
            @RequestBody SupplyValidationDto updateDto
    ) {
        SupplyResponseDto updated = supplyService.validateSupply(
                id,
				updateDto.approverId(),
				updateDto.status(),
				updateDto.comment()
        );
        return ResponseEntity.ok(updated);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SupplyResponseDto> updateSupply(
            @PathVariable Long id,
            @RequestBody SupplyRequestDto updatedDto
    ) {
        return ResponseEntity.ok(supplyService.updateSupply(id, updatedDto));
    }
    
    @GetMapping("/{supplyId}/files")
    public ResponseEntity<List<String>> listSupplyFiles(@PathVariable Long supplyId) {
        List<String> files = supplyService.getSupplyImages(supplyId);
        return ResponseEntity.ok(files);
    }
    
    @GetMapping("/{supplyId}/files/{fileName}")
    public ResponseEntity<Resource> downloadSupplyFile(
            @PathVariable Long supplyId,
            @PathVariable String fileName) {

        Resource resource = supplyService.getSupplyFile(supplyId, fileName);
        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = supplyService.getContentType(resource);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

}
