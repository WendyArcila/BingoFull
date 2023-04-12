package com.sofka.bingo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;
import java.io.Serializable;

/**
 * Esta clase representa el estado de una entidad de juego, tablero o jugador.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Data
@Entity
@Table(name = "status")
public class Status implements  Serializable {
    private static final Long serialVersionUID = 1L;

    /**
     * El ID del juego. Es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "statu_id")
    private Long idStatus;

    @Column(name = "statu_name")
    private String statusName;

    @Column(name = "statu_description")
    private String statusDescription;

    // Los getters y setters para todos los atributos son generados por Lombok.
}
