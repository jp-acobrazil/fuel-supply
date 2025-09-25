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
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.acobrazil.fuelsupply.repositories.SupplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.acobrazil.fuelsupply.models.exceptions.DriverNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@RequiredArgsConstructor
public class SupplyService {

	private final SupplyRepository supplyRepository;
	private final DriverRepository driverRepository;
	private final VehicleRepository vehicleRepository;

	@Value("${upload.directory}")
	private String uploadDir;

	public SupplyResponseDto createSupply(SupplyRequestDto supply) {
		log.info("Iniciando criação de abastecimento para motoristaId={}, placa={}", supply.driverId(), supply.plate());

		Supply supplyEntity = Supply.builder().date(LocalDateTime.now()).driverId(supply.driverId())
				.liters(supply.liters()).pricePerLiter(supply.pricePerLiter()).plate(supply.plate())
				.fuelType(supply.fuelType()).odometer(supply.odometer()).loadNumber(null).inRoute("N")
				.stationCnpj(supply.stationCnpj()).stationName(null).obs(supply.obs()).build();
		
		log.info("Entidade de abastecimento criada: {}", supplyEntity);

		Driver driver = driverRepository.findById(supply.driverId()).orElseThrow(() -> {
			log.error("Motorista não encontrado! driverId={}", supply.driverId());
			return new DriverNotFoundException("Driver not found with id: " + supply.driverId());
		});
		supplyEntity.setDriver(driver);
		log.debug("Motorista encontrado: {}", driver.getName());

		Vehicle vehicle = vehicleRepository.findByPlate(supply.plate()).orElseThrow(() -> {
			log.error("Veículo não encontrado! plate={}", supply.plate());
			return new VehicleNotFoundException("Vehicle not found with plate: " + supply.plate());
		});
		supplyEntity.setVehicle(vehicle);
		log.debug("Veículo encontrado: {}", vehicle.getDescription());

		Supply savedSupply = supplyRepository.save(supplyEntity);
		log.info("Abastecimento salvo com sucesso! id={}", savedSupply.getId());

		return toDto(savedSupply);
	}

	public List<SupplyResponseDto> getAllSupplies() {
		return StreamSupport.stream(supplyRepository.findAll().spliterator(), false).map(this::toDto).toList();
	}

	public SupplyResponseDto getSupplyById(Long id) {
		Supply supply = supplyRepository.findById(id)
				.orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));
		return toDto(supply);
	}

	public SupplyResponseDto toDto(Supply supply) {
		Driver driver = supply.getDriver();
		DriverDto driverDto = new DriverDto(driver.getDriverId(), driver.getName());

		Vehicle vehicle = supply.getVehicle();
		VehicleDto vehicleDto = new VehicleDto(vehicle.getId(), vehicle.getPlate(), vehicle.getCarType(),
				vehicle.getDescription(), vehicle.getIsOwn());

		return new SupplyResponseDto(supply.getId(), supply.getDate(), supply.getLiters(), supply.getPricePerLiter(),
				supply.getFuelType(), supply.getOdometer(), supply.getObs(), driverDto, vehicleDto);
	}

	public void saveSupplyPhotos(Long supplyId, MultipartFile pumpPhoto, MultipartFile odometerPhoto,
			List<MultipartFile> attachments) {

		Path supplyDir = Paths.get(uploadDir, "supply_" + supplyId);
		try {
			Files.createDirectories(supplyDir);
			log.debug("Diretório de fotos criado em {}", supplyDir.toAbsolutePath());
		} catch (IOException e) {
			log.error("Erro ao criar diretório de fotos para supplyId={}", supplyId, e);
			throw new RuntimeException("Erro ao criar diretório de fotos", e);
		}

		try {
			if (pumpPhoto != null && !pumpPhoto.isEmpty()) {
				saveFile(supplyDir, "pumpPhoto", pumpPhoto);
				log.info("Foto da bomba salva com sucesso.");
			}
			if (odometerPhoto != null && !odometerPhoto.isEmpty()) {
				saveFile(supplyDir, "odometerPhoto", odometerPhoto);
				log.info("Foto do odômetro salva com sucesso.");
			}
			if (attachments != null) {
				int idx = 1;
				for (MultipartFile file : attachments) {
					if (file != null && !file.isEmpty()) {
						saveFile(supplyDir, "attach_" + idx, file);
						log.info("Anexo {} salvo com sucesso: {}", idx, file.getOriginalFilename());
						idx++;
					}
				}
			}
		} catch (IOException e) {
			log.info("IOException ao salvar fotos do abastecimento supplyId={}", supplyId, e);
			log.error("Erro ao salvar fotos do abastecimento supplyId={}", supplyId, e);
			throw new RuntimeException("Erro ao salvar fotos do abastecimento", e);
		}
	}

	private void saveFile(Path dir, String prefix, MultipartFile file) throws IOException {
		String originalName = file.getOriginalFilename();
		String ext = "";

		if (originalName != null && originalName.contains(".")) {
			ext = originalName.substring(originalName.lastIndexOf("."));
		}

		Path filePath = dir.resolve(prefix + ext);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
	}

}