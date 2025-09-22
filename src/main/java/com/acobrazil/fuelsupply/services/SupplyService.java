package com.acobrazil.fuelsupply.services;

import com.acobrazil.fuelsupply.models.Driver;
import com.acobrazil.fuelsupply.models.Supply;
import com.acobrazil.fuelsupply.models.Vehicle;
import com.acobrazil.fuelsupply.models.dtos.DriverDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyResponseDto;
import com.acobrazil.fuelsupply.models.dtos.VehicleDto;
import com.acobrazil.fuelsupply.models.exceptions.SupplyNotFoundException;
import com.acobrazil.fuelsupply.models.exceptions.VehicleNotFoundException;
import com.acobrazil.fuelsupply.repositories.DriverRepository;
import com.acobrazil.fuelsupply.repositories.VehicleRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import com.acobrazil.fuelsupply.repositories.SupplyRepository;
import lombok.AllArgsConstructor;
import com.acobrazil.fuelsupply.models.exceptions.DriverNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
public class SupplyService {
	
	private final SupplyRepository supplyRepository;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public SupplyResponseDto createSupply(SupplyRequestDto supply, HttpServletRequest request) {
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

        Vehicle vehicle = vehicleRepository.findByPlate(supply.plate()).orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with plate: " + supply.plate()));
        supplyEntity.setVehicle(vehicle);

        Supply savedSupply = supplyRepository.save(supplyEntity);
        return (toDto(savedSupply));
    }

    public List<SupplyResponseDto> getAllSupplies() {
        return StreamSupport.stream(supplyRepository.findAll().spliterator(), false)
                .map(this::toDto)
                .toList();
    }

    public SupplyResponseDto getSupplyById(Long id) {
        Supply supply = supplyRepository.findById(id)
                .orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));
        return toDto(supply);
    }

    public SupplyResponseDto toDto(Supply supply) {
        Driver driver = supply.getDriver(); // lazy loading aqui, vai buscar do DB se necessário
        DriverDto driverDto = new DriverDto(
                driver.getDriverId(),
                driver.getName()
        );

        Vehicle vehicle = supply.getVehicle();
        VehicleDto vehicleDto = new VehicleDto(vehicle.getId(), vehicle.getPlate(), vehicle.getCarType(), vehicle.getDescription(), vehicle.getIsOwn());

        return new SupplyResponseDto(
                supply.getId(),
                supply.getDate(),
                supply.getLiters(),
                supply.getPricePerLiter(),
                supply.getFuelType(),
                supply.getOdometer(),
                supply.getObs(),
                driverDto,
                vehicleDto
        );
    }
}