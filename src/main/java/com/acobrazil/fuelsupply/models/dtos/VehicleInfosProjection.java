package com.acobrazil.fuelsupply.models.dtos;

import java.time.LocalDateTime;

public interface VehicleInfosProjection {
	LocalDateTime getDataUltimoAbastecimento();
	String getUltimoHodometroRegistrado();
	String getPlaca();
	String getModeloVeiculo();
	String getTipoVeiculo();
}
