package com.sofka.bingo.domain;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;
import java.time.Instant;


/**
 * La clase Gamer representa un jugador en un juego.
 * Un jugador tiene un ID único, una fecha de creación, una fecha de actualización, un nombre de usuario, un estado de juego y un tablero.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Getter
@Setter
@Entity
@Table(name = "gamer")
public class Gamer implements Serializable {

    private static final Long serialVersionUID = 1L;

    /**
     * El ID del juego. Es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_gamer")
    private Long idGamer;

    @Column(name = "creation_date")
    private Instant creationAt;

    @Column(name = "update_date")
    private Instant updateAt;


    @Column(name = "user")
    private String user;

    /**
     * El estado del jugador en el juego.
     */
    @OneToOne
    @JoinColumn(name = "statu_id")
    private Status statusGamer;

    /**
     * El tablero del jugador.
     */
    @OneToOne
    @JoinColumn(name = "boa_id")
    private Board board;

    /**
     * El juego en el que está jugando el jugador.
     */
    @JsonBackReference(value="game")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gam_id")
    private Game game;

    // Los getters y setters para todos los atributos son generados por Lombok.
}
