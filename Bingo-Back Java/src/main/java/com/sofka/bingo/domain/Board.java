package com.sofka.bingo.domain;


import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * Esta clase representa una entidad de Tablero que se mapea a la tabla "tablero" de la base de datos.
 * Incluye el ID del tablero, el ID del jugador y los 25 números del tablero organizados en una cuadrícula de 5x5.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */

@Getter
@Setter
@Entity
@Table(name = "board")
public class Board implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * El ID del juego. Es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "boa_id")
    private Long idBoard;

    @Column(name = "id_gamer")
    private Long gamerId;

    @Column(name = "number_b1")
    private int numberB1;

    @Column(name = "number_i1")
    private int numberI1;

    @Column(name = "number_n1")
    private int numberN1;

    @Column(name = "number_g1")
    private int numberG1;

    @Column(name = "number_o1")
    private int numberO1;

    @Column(name = "number_b2")
    private int numberB2;

    @Column(name = "number_i2")
    private int numberI2;

    @Column(name = "number_n2")
    private int numberN2;

    @Column(name = "number_g2")
    private int numberG2;

    @Column(name = "number_o2")
    private int numberO2;

    @Column(name = "number_b3")
    private int numberB3;

    @Column(name = "number_i3")
    private int numberI3;

    @Column(name = "number_n3")
    private int numberN3;

    @Column(name = "number_g3")
    private int numberG3;

    @Column(name = "number_o3")
    private int numberO3;

    @Column(name = "number_b4")
    private int numberB4;

    @Column(name = "number_i4")
    private int numberI4;

    @Column(name = "number_n4")
    private int numberN4;

    @Column(name = "number_g4")
    private int numberG4;

    @Column(name = "number_o4")
    private int numberO4;

    @Column(name = "number_b5")
    private int numberB5;

    @Column(name = "number_i5")
    private int numberI5;

    @Column(name = "number_n5")
    private int numberN5;

    @Column(name = "number_g5")
    private int numberG5;

    @Column(name = "number_o5")
    private int numberO5;

    // Los getters y setters para todos los atributos son generados por Lombok.

}
