package com.acobrazil.fuelsupply.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "abz_abastecimento")
@SequenceGenerator(name="SUPPLY_ID_GENERATOR", sequenceName = "abz_abastecimento_seq", allocationSize = 1)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SUPPLY_ID_GENERATOR")
    @Column(name = "codabastecimento")
    private Long id;

    @Column(name = "data", nullable = false)
    private LocalDateTime date;

    @Column(name = "codmotorista", nullable = false, insertable = false, updatable = false)
    private Integer driverId;

    @Column(name = "qtlitros", nullable = false, precision = 8, scale = 2)
    private BigDecimal liters;

    @Column(name = "precolitro", nullable = false, precision = 8, scale = 3)
    private BigDecimal pricePerLiter;

    @Column(name = "placa", nullable = false, length = 7, insertable = false, updatable = false)
    private String plate;

    @Column(name = "tipocombustivel", nullable = false, length = 50)
    private String fuelType;

    @Column(name = "hodometro", precision = 12)
    private Long odometer;

    @Column(name = "horimetro", precision = 12)
    private Long hourmeter;

    @Column(name = "numcar")
    private Integer loadNumber;

    @Column(name = "emrota", length = 1)
    private String inRoute; // pode ser 'S'/'N'

    @Column(name = "cnpjposto", length = 14)
    private String stationCnpj;

    @Column(name = "nomeposto", length = 50)
    private String stationName;

    @Column(name = "obs", length = 200)
    private String obs;
    
    @Column(name = "status", length = 1)
    private char status;

    @Column(name = "codfuncaprov")
    private Integer approverId;
    
    @Column(name = "dataaprovacao")
    private LocalDateTime approvalDate;
    
    @Column(name = "comentario_aprovacao", length = 500)
    private String approvalComment;
    
    /** RELACIONAMENTOS **/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "placa", referencedColumnName = "placa")
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codmotorista", referencedColumnName = "matricula")
    private Employee driver;
}