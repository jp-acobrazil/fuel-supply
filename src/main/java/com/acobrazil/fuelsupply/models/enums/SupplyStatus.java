package com.acobrazil.fuelsupply.models.enums;

import lombok.Getter;

@Getter
public enum SupplyStatus {
	
	CREATED('C'),
	APPROVED('A'),
	REJECTED('R');
	
	private final char status;
	
	SupplyStatus(char status) {
		this.status = status;
	}

}
