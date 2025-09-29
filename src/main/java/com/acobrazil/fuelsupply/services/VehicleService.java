package com.acobrazil.fuelsupply.services;

import org.springframework.stereotype.Service;

import com.acobrazil.fuelsupply.models.dtos.VehicleInfosProjection;
import com.acobrazil.fuelsupply.repositories.VehicleRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VehicleService {
	
	private final VehicleRepository vehicleRepository;
	
	public VehicleInfosProjection getVehicleInfos(String plate) {
		return vehicleRepository.findVehicleInfos(plate);
	}

}
