package com.sofka.bingo.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.Data;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * La clase Game representa un juego en el sistema.
 * Se asigna a la tabla "juego" de la base de datos mediante anotaciones JPA.
 * Contiene el ID del juego, ganador, fecha de creación, fecha de actualización, estado, jugadores y movimientos.
 * @version 1.0.000 2023-01-20
 * @author Wendy Arcila
 */
@Data
@Entity
@Table(name = "game")
public class Game implements Serializable {
    private static final Long serialVersionUID = 1L;

    /**
     * El ID del juego. Es generado automáticamente por la base de datos.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "gam_id")
    private Long idGame;

    private String winner;

    @Column(name = "creation_date")
    private Instant creationAt;

    @Column(name = "update_date")
    private Instant updateAt;

    /**
     * El estado del juego, representado por un objeto Status.
     */
    @OneToOne
    @JoinColumn(name = "statu_id")
    private Status statusGame;

    /**
     * La lista de jugadores que participan en el juego, representada por una lista de objetos Gamer.
     */
    @JsonManagedReference(value="game")
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "game",
            cascade = CascadeType.ALL)
    private List<Gamer> gamers = new ArrayList<>();

    /**
     * La lista de jugadas realizadas durante la partida, representada por una lista de objetos Move.
     */
    @JsonManagedReference(value="move")
    @OneToMany(
            fetch = FetchType.EAGER,
            mappedBy = "game",
            cascade = CascadeType.ALL)
    private List<Move> moves = new ArrayList<>();

    // Los getters y setters para todos los atributos son generados por Lombok.
}


