package com.acobrazil.fuelsupply.services;

import com.acobrazil.fuelsupply.models.Driver;
import com.acobrazil.fuelsupply.models.Supply;
import com.acobrazil.fuelsupply.models.Vehicle;
import com.acobrazil.fuelsupply.models.dtos.DriverDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyRequestDto;
import com.acobrazil.fuelsupply.models.dtos.SupplyResponseDto;
import com.acobrazil.fuelsupply.models.dtos.VehicleDto;
import com.acobrazil.fuelsupply.models.enums.SupplyStatus;
import com.acobrazil.fuelsupply.models.exceptions.SupplyNotFoundException;
import com.acobrazil.fuelsupply.models.exceptions.VehicleNotFoundException;
import com.acobrazil.fuelsupply.repositories.DriverRepository;
import com.acobrazil.fuelsupply.repositories.VehicleRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
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
import java.util.stream.Stream;
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
		
		Vehicle vehicle = vehicleRepository.findByPlate(supply.plate()).orElseThrow(() -> {
			log.error("Veículo não encontrado! plate={}", supply.plate());
			return new VehicleNotFoundException("Vehicle not found with plate: " + supply.plate());
		});
		
		log.debug("Veículo encontrado: {} - Tipo: {}", vehicle.getDescription(), vehicle.getPlate(), vehicle.getCarType());
		
		if("X".equals(vehicle.getCarType())) {
			if(supply.hourmeter() == null) {
				log.error("Abastecimento de máquina exige horímetro. Abastecimento rejeitado.");
				throw new IllegalArgumentException("Abastecimento de máquina exige horímetro.");
			}
		} else {
			if(supply.odometer() == null) {
				log.error("Abastecimento de veículo exige odômetro. Abastecimento rejeitado.");
				throw new IllegalArgumentException("Abastecimento de veículo exige odômetro.");
			}
		}
		
	    Supply supplyEntity = Supply.builder()
	            .date(LocalDateTime.now())
	            .driverId(supply.driverId())
	            .liters(supply.liters())
	            .pricePerLiter(supply.pricePerLiter())
	            .plate(supply.plate())
	            .fuelType(supply.fuelType())
	            .odometer(supply.odometer())
	            .hourmeter(supply.hourmeter())
	            .loadNumber(null)
	            .inRoute("N")
	            .stationCnpj(supply.stationCnpj())
	            .stationName(supply.stationName())
	            .obs(supply.obs())
	            .status(SupplyStatus.CREATED.getStatus())
	            .build();

		log.info("Entidade de abastecimento criada: {}", supplyEntity);

		Driver driver = driverRepository.findById(supply.driverId()).orElseThrow(() -> {
			log.error("Motorista não encontrado! driverId={}", supply.driverId());
			return new DriverNotFoundException("Driver not found with id: " + supply.driverId());
		});
		
		supplyEntity.setDriver(driver);
		log.debug("Motorista encontrado: {}", driver.getName());

		supplyEntity.setVehicle(vehicle);

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

	public SupplyResponseDto updateSupply(Long id, SupplyRequestDto updatedDto) {
		Supply existing = supplyRepository.findById(id)
				.orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));

		// só permite edição se o status for REJECTED
		if (existing.getStatus() != SupplyStatus.REJECTED.getStatus()) {
			throw new IllegalStateException("Só é possível editar um abastecimento REPROVADO.");
		}

		existing.setLiters(updatedDto.liters());
		existing.setPricePerLiter(updatedDto.pricePerLiter());
		existing.setPlate(updatedDto.plate());
		existing.setFuelType(updatedDto.fuelType());
		existing.setOdometer(updatedDto.odometer());
		existing.setStationCnpj(updatedDto.stationCnpj());
		existing.setObs(updatedDto.obs());

		// Resetar status para "CREATED" quando motorista reenvia
		existing.setStatus(SupplyStatus.CREATED.getStatus());
		existing.setApproverId(null);
		existing.setApprovalComment(null);

		Supply updated = supplyRepository.save(existing);
		return toDto(updated);
	}

	public SupplyResponseDto validateSupply(Long id, Integer approverId, SupplyStatus status, String comment) {
		Supply supply = supplyRepository.findById(id)
				.orElseThrow(() -> new SupplyNotFoundException("Supply not found with id: " + id));

		supply.setApproverId(approverId);
		supply.setStatus(status.getStatus());
		supply.setApprovalComment(comment);

		Supply updated = supplyRepository.save(supply);
		return toDto(updated);
	}

	public SupplyResponseDto toDto(Supply supply) {
		Driver driver = supply.getDriver();
		DriverDto driverDto = new DriverDto(driver.getDriverId(), driver.getName());

		Vehicle vehicle = supply.getVehicle();
		VehicleDto vehicleDto = new VehicleDto(vehicle.getId(), vehicle.getPlate(), vehicle.getCarType(),
				vehicle.getDescription(), vehicle.getIsOwn());

		return new SupplyResponseDto(
				supply.getId(), 
				supply.getDate(), 
				supply.getLiters(), 
				supply.getPricePerLiter(),
				supply.getFuelType(), 
				supply.getOdometer(),
				supply.getHourmeter(),
				supply.getLoadNumber(),
				supply.getInRoute(),
				supply.getStationCnpj(),
				supply.getStationName(),
				supply.getObs(), 
				supply.getStatus(), 
				supply.getApproverId(),
				supply.getApprovalComment(), 
				driverDto, vehicleDto);
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

		// Apaga qualquer arquivo existente com o mesmo prefixo (independente da
		// extensão)
		try (Stream<Path> files = Files.list(dir)) {
			files.filter(path -> path.getFileName().toString().startsWith(prefix)).forEach(existing -> {
				try {
					Files.delete(existing);
					log.debug("Arquivo antigo deletado: {}", existing.getFileName());
				} catch (IOException e) {
					log.warn("Não foi possível deletar arquivo antigo: {}", existing, e);
				}
			});
		}

		Path filePath = dir.resolve(prefix + ext);
		Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

		log.info("Novo arquivo salvo: {}", filePath.getFileName());
	}

	public List<String> getSupplyImages(Long supplyId) {
		Path supplyDir = Paths.get(uploadDir, "supply_" + supplyId);
		if (!Files.exists(supplyDir)) {
			return List.of();
		}

		try (Stream<Path> files = Files.list(supplyDir)) {
			return files.filter(Files::isRegularFile).map(path -> path.getFileName().toString()).toList();
		} catch (IOException e) {
			log.error("Erro ao listar imagens do supplyId={}", supplyId, e);
			return List.of();
		}
	}

	public Resource getSupplyFile(Long supplyId, String fileName) {
		Path filePath = Paths.get(uploadDir, "supply_" + supplyId, fileName);

		if (!Files.exists(filePath)) {
			return null; // controller decide como responder
		}

		try {
			return new FileSystemResource(filePath);
		} catch (Exception e) {
			log.error("Erro ao carregar arquivo {} do supplyId={}", fileName, supplyId, e);
			throw new RuntimeException("Erro ao carregar arquivo", e);
		}
	}

	public String getContentType(Resource resource) {
		try {
			Path path = resource.getFile().toPath();
			return Files.probeContentType(path);
		} catch (IOException e) {
			log.warn("Não foi possível detectar content type para {}", resource.getFilename());
			return "application/octet-stream";
		}
	}

	public List<SupplyResponseDto> getSuppliesByDriverId(Long driverId) {
		List<Supply> supplies = supplyRepository.findByDriverId(driverId);
		return supplies.stream().map(this::toDto).toList();
	}

}