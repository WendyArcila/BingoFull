package com.sofka.bingo.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import java.io.Serializable;

/**
 * Esta clase representa un movimiento dentro del juego.
 * Cada movimiento está relacionado con un juego (Game) y tiene una letra y un número
 * que representan la posición en la que se hizo el movimiento.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Data
@Entity
@Table(name = "move")
public class Move implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * El ID del juego. Es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//autoincrementa el id
    @Column(name = "mov_id")
    private Long idMove;

    @Column(name = "mov_letter")
    private String letter;

    @Column(name = "mov_number")
    private int number;

    /**
     * Juego (Game) al que está relacionado el movimiento.
     */
    @JsonBackReference(value="move")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gam_id")
    private Game game;

    // Los getters y setters para todos los atributos son generados por Lombok.
}

