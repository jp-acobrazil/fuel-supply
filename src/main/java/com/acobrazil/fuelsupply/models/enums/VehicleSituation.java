package com.acobrazil.fuelsupply.models.enums;

import lombok.Getter;

@Getter
public enum VehicleSituation {
    BLOCKED('B'),
    AVAILABLE('L'),
    IN_WORKSHOP('O'),
    IN_TRAVELING('T'), //Este status existe apenas a nível de aplicação. náo é persistido no banco de dados
    MOUNTED('V'),
    INACTIVE('I');

    private final char situation;

    VehicleSituation(char situation) {this.situation = situation;}

	public char getSituation() {
		return situation;
	}
}
