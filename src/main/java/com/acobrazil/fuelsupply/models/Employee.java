package com.acobrazil.fuelsupply.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pcempr")
@Data
@Setter
@Getter
public class Employee {

    @Id
    @Column(name = "matricula")
    private int id;

    @Column(name = "nome")
    private String name;

    @Column(name = "situacao")
    private String isActive;

    @Column(name = "codsetor")
    private int sectorId;

    @Column(name = "codfilial")
    private Integer officeCode;

}
