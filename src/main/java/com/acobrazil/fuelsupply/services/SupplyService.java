package com.acobrazil.fuelsupply.services;

import com.acobrazil.fuelsupply.models.Driver;
import com.acobrazil.fuelsupply.models.Supply;
import com.acobrazil.fuelsupply.models.Vehicle;
import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.repositories.DriverRepository;
import com.acobrazil.fuelsupply.repositories.VehicleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.acobrazil.fuelsupply.repositories.SupplyRepository;
import lombok.AllArgsConstructor;
import com.acobrazil.fuelsupply.models.exceptions.DriverNotFoundException;
import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class SupplyService {
	
	private final SupplyRepository supplyRepository;

    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public Supply createSupply(SupplyRequestDto supply, HttpServletRequest request) {
        Supply supplyEntity = Supply.builder()
                .date(LocalDateTime.now())
                .driverId(supply.driverId())
                .liters(supply.liters())
                .pricePerLiter(supply.pricePerLiter())
                .plate(supply.plate())
                .fuelType(supply.fuelType())
                .odometer(supply.odometer())
                .loadNumber(null)
                .inRoute("N")
                .stationCnpj(supply.stationCnpj())
                .stationName(null)
                .obs(null)
                .build();

        Driver driver = driverRepository.findById(supply.driverId()).orElseThrow(() -> new DriverNotFoundException("Driver not found with id: " + supply.driverId()));
        supplyEntity.setDriver(driver);

        Vehicle vehicle = vehicleRepository.findByPlate(supply.plate()).orElseThrow(() -> new RuntimeException("Vehicle not found with plate: " + supply.plate()));
        supplyEntity.setVehicle(vehicle);

        System.out.println("Creating supply: " + supplyEntity);

        return supplyRepository.save(supplyEntity);
    }

}
