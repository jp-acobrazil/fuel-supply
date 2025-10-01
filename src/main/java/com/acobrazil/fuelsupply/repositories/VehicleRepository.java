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
	    SELECT 
	        v.placa,
	        v.descricao AS modeloVeiculo,
	        v.tipoveiculo,
	        a.data AS dataUltimoAbastecimento,
	        a.hodometro AS ultimoHodometroRegistrado
	    FROM pcveicul v
	    LEFT JOIN (
	        SELECT 
	            a.placa,
	            a.data,
	            a.hodometro,
	            ROW_NUMBER() OVER (PARTITION BY a.placa ORDER BY a.data DESC) AS rn
	        FROM abz_abastecimento a
	    ) a ON v.placa = a.placa AND a.rn = 1
	    WHERE v.placa = :plate
	    """, nativeQuery = true)
	VehicleInfosProjection findVehicleInfos(String plate);

}
