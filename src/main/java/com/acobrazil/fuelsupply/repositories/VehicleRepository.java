package com.acobrazil.fuelsupply.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.acobrazil.fuelsupply.models.Vehicle;
import com.acobrazil.fuelsupply.models.dtos.VehicleInfosProjection;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehicleRepository extends CrudRepository<Vehicle, Integer> {

	Optional<Vehicle> findByPlate(String plate);

	@Query(value = """
			SELECT data as dataUltimoAbastecimento,
			hodometro as ultimoHodometroRegistrado,
			placa,
			descricao as modeloVeiculo
			FROM (
			SELECT a.data,
			a.hodometro,
			a.placa,
			v.descricao,
			ROW_NUMBER() OVER (PARTITION BY a.placa ORDER BY a.data DESC) AS rn
			FROM abz_abastecimento a
			JOIN pcveicul v ON a.placa = v.placa
			) t
			WHERE rn = 1
			AND PLACA = :plate
			--ORDER BY data DESC;
			""", nativeQuery = true)
	VehicleInfosProjection findVehicleInfos(String plate);
}
