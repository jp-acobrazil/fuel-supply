package com.acobrazil.fuelsupply.models;

import com.acobrazil.fuelsupply.models.enums.VehicleSituation;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table(name = "pcveicul")
public class Vehicle {
	
    @Id
    @Column(name = "codveiculo")
    private int id;

    @Column(name = "placa")
    private String plate;

    @Column(name = "situacao")
    private char situation;

    @Column(name = "tipoveiculo")
    private String carType;

    @Column(name = "pesocargakg")
    private Integer capacity;

    @Column(name = "descricao")
    private String description;

    @Column(name = "proprio")
    private char isOwn;

    @Column(name = "codfilial")
    private String branchOfficeId;

    @Column(name = "largura")
    private Double width;

    @Column(name = "comprimento")
    private Double length;

    public boolean hasCargos() {
        return (this.situation == VehicleSituation.MOUNTED.getSituation() || this.situation == VehicleSituation.IN_TRAVELING.getSituation());
    }

}
