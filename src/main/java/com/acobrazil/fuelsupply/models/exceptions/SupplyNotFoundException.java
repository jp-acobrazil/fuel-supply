package com.acobrazil.fuelsupply.models.exceptions;

public class SupplyNotFoundException extends RuntimeException{
    public SupplyNotFoundException(String message) {
        super(message);
    }
}
