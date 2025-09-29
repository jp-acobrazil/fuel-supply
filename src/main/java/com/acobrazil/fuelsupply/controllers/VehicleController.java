package com.acobrazil.fuelsupply.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.acobrazil.fuelsupply.models.dtos.VehicleInfosProjection;
import com.acobrazil.fuelsupply.services.VehicleService;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/vehicles")
@AllArgsConstructor
public class VehicleController {
	
	private final VehicleService vehicleService;

	@GetMapping("/{plate}/infos")
	public VehicleInfosProjection getVehicleInfos(@PathVariable String plate) {
	    return vehicleService.getVehicleInfos(plate);
	}

}
